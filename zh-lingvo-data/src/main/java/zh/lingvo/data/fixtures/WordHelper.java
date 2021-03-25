package zh.lingvo.data.fixtures;

import zh.lingvo.data.model.Example;
import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.SemanticBlock;
import zh.lingvo.data.model.Translation;
import zh.lingvo.data.model.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WordHelper {
    private WordHelper() {
    }

    public static void add(Meaning meaning, Translation... translations) {
        Set<Translation> setTranslations = getSafely(meaning::getTranslations, LinkedHashSet::new, meaning::setTranslations);
        Collections.addAll(setTranslations, translations);
    }

    public static void add(Meaning meaning, Example... examples) {
        Set<Example> setExamples = getSafely(meaning::getExamples, LinkedHashSet::new, meaning::setExamples);
        Collections.addAll(setExamples, examples);
    }

    public static void add(SemanticBlock sBlock, Meaning... meanings) {
        List<Meaning> setMeanings = getSafely(sBlock::getMeanings, ArrayList::new, sBlock::setMeanings);
        Collections.addAll(setMeanings, meanings);
    }

    public static void add(Word word, SemanticBlock... blocks) {
        List<SemanticBlock> setBlocks = getSafely(word::getSemanticBlocks, ArrayList::new, word::setSemanticBlocks);
        Collections.addAll(setBlocks, blocks);
    }

    private static <E, C extends Collection<E>> C getSafely(Supplier<C> setSupplier, Supplier<C> fallbackSetSupplier, Consumer<C> setSetter) {
        C collection = setSupplier.get();
        if (collection != null)
            return collection;
        collection = fallbackSetSupplier.get();
        setSetter.accept(collection);
        return collection;
    }
}
