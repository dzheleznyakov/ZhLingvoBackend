package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Service;
import zh.lingvo.data.fixtures.Persistable;
import zh.lingvo.data.fixtures.SubWordPart;
import zh.lingvo.data.fixtures.SubWordRepository;
import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Translation;
import zh.lingvo.data.services.SubWordService;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.MoreObjects.firstNonNull;

@Service
public class SubWordServiceImpl implements SubWordService {
    private final Map<Class<?>, SubWordRepository<?, ?>> subWordRepositories;

    public SubWordServiceImpl(Set<? extends SubWordRepository<?, ?>> subWordRepositories) {
        this.subWordRepositories = subWordRepositories.stream()
                .collect(ImmutableMap.toImmutableMap(SubWordRepository::getSubWordClass, Function.identity()));
    }

    @Override
    public <E extends SubWordPart> Optional<E> save(E entity) {
        E savedEntity = getRepository(entity.getClass()).save(entity);
        saveChildSubWordParts(entity);
        return Optional.of(savedEntity);
    }

    @SuppressWarnings("unchecked")
    private <E extends SubWordPart> SubWordRepository<E, ?> getRepository(Class<? extends SubWordPart> entityClass) {
        return (SubWordRepository<E, ?>) subWordRepositories.get(entityClass);
    }

    private <E extends Persistable & SubWordPart> void saveChildSubWordParts(E parent) {
        LinkedList<SubWordPart> stack = getSubWordParts(parent);
        Set<SubWordPart> withExtractedParts = new HashSet<>();
        while (!stack.isEmpty())
            processStackTop(stack, withExtractedParts);
    }

    private void processStackTop(LinkedList<SubWordPart> stack, Set<SubWordPart> withExtractedParts) {
        SubWordPart swp = stack.removeLast();
        LinkedList<SubWordPart> moreParts = getSubWordParts(swp);
        if (moreParts.isEmpty() || withExtractedParts.contains(swp))
            getRepository(swp.getClass()).save(swp);
        else {
            stack.addAll(moreParts);
            stack.addLast(swp);
            withExtractedParts.add(swp);
        }
    }

    private <E extends Persistable & SubWordPart> LinkedList<SubWordPart> getSubWordParts(E parent) {
        if (parent instanceof Meaning)
            return getMeaningSubWordParts((Meaning) parent);
        if (parent instanceof SemanticBlock)
            return getSemanticBlockSubWordParts((SemanticBlock) parent);
        return new LinkedList<>();
    }

    private LinkedList<SubWordPart> getMeaningSubWordParts(Meaning meaning) {
        LinkedList<SubWordPart> parts = new LinkedList<>();
        firstNonNull(meaning.getExamples(), ImmutableSet.<Example>of())
                .stream()
                .peek(example -> example.setMeaning(meaning))
                .forEach(parts::addFirst);
        firstNonNull(meaning.getTranslations(), ImmutableSet.<Translation>of())
                .stream()
                .peek(translation -> translation.setMeaning(meaning))
                .forEach(parts::addFirst);
        return parts;
    }

    private LinkedList<SubWordPart> getSemanticBlockSubWordParts(SemanticBlock block) {
        LinkedList<SubWordPart> parts = new LinkedList<>();
        firstNonNull(block.getMeanings(), ImmutableList.<Meaning>of())
                .stream()
                .peek(meaning -> meaning.setSemBlock(block))
                .forEach(parts::addFirst);
        return parts;
    }
}
