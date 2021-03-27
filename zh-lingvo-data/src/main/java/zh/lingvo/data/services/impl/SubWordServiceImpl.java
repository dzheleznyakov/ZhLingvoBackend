package zh.lingvo.data.services.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Service;
import zh.lingvo.data.fixtures.SubWordPart;
import zh.lingvo.data.fixtures.SubWordRepository;
import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Translation;
import zh.lingvo.data.services.SubWordService;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        Map<Class<? extends SubWordPart>, LinkedList<SubWordPart>> subWordPartsByClass = getSubWordPartsByClass(entity);
        E savedEntity = getRepository(entity.getClass()).save(entity);
        ImmutableList.of(SemanticBlock.class, Meaning.class, Translation.class, Example.class).forEach(cl -> {
            SubWordRepository<SubWordPart, ?> repository = getRepository(cl);
            LinkedList<SubWordPart> subWordParts = subWordPartsByClass.getOrDefault(cl, new LinkedList<>());
            if (!subWordParts.isEmpty())
                repository.saveAll(subWordParts);
        });
        return Optional.of(savedEntity);
    }

    private <E extends SubWordPart> Map<Class<? extends SubWordPart>, LinkedList<SubWordPart>> getSubWordPartsByClass(E entity) {
        Map<Class<? extends SubWordPart>, LinkedList<SubWordPart>> map = new HashMap<>();
        LinkedList<SubWordPart> stack = new LinkedList<>();
        stack.addFirst(entity);
        while (!stack.isEmpty())
            processStackTop(stack, map);
        return map;
    }

    private void processStackTop(LinkedList<SubWordPart> stack, Map<Class<? extends SubWordPart>, LinkedList<SubWordPart>> map) {
        SubWordPart swp = stack.removeLast();
        getSubWordParts(swp).forEach(part -> {
            map.putIfAbsent(part.getClass(), new LinkedList<>());
            map.get(part.getClass()).addFirst(part);
            stack.add(part);
        });
    }

    @SuppressWarnings("unchecked")
    private <E extends SubWordPart> SubWordRepository<E, ?> getRepository(Class<? extends SubWordPart> entityClass) {
        return (SubWordRepository<E, ?>) subWordRepositories.get(entityClass);
    }

    private <E extends SubWordPart> LinkedList<SubWordPart> getSubWordParts(E parent) {
        if (parent instanceof Meaning)
            return getMeaningSubWordParts((Meaning) parent);
        if (parent instanceof SemanticBlock)
            return getSemanticBlockSubWordParts((SemanticBlock) parent);
        return new LinkedList<>();
    }

    private LinkedList<SubWordPart> getMeaningSubWordParts(Meaning meaning) {
        LinkedList<SubWordPart> examples = getSubWordParts(meaning::getExamples, e -> e.setMeaning(meaning));
        LinkedList<SubWordPart> translations = getSubWordParts(meaning::getTranslations, t -> t.setMeaning(meaning));
        examples.addAll(translations);
        return examples;
    }

    private LinkedList<SubWordPart> getSemanticBlockSubWordParts(SemanticBlock block) {
        return getSubWordParts(block::getMeanings, m -> m.setSemBlock(block));
    }

    private <CH extends SubWordPart, C extends Collection<CH>> LinkedList<SubWordPart> getSubWordParts(Supplier<C> colSupplier, Consumer<CH> setter) {
        LinkedList<SubWordPart> parts = new LinkedList<>();
        firstNonNull(colSupplier.get(), ImmutableList.<CH>of())
                .stream()
                .peek(setter)
                .forEach(parts::addFirst);
        return parts;
    }
}
