package zh.lingvo.controllers;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.util.ApiMapping;
import zh.lingvo.caches.DictionaryCache;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.words.Example;
import zh.lingvo.domain.words.Meaning;
import zh.lingvo.domain.words.Name;
import zh.lingvo.domain.words.PosBlock;
import zh.lingvo.domain.words.SemanticBlock;
import zh.lingvo.domain.words.Transcription;
import zh.lingvo.domain.words.Translation;
import zh.lingvo.domain.words.Word;
import zh.lingvo.domain.words.WordEntity;
import zh.lingvo.persistence.Writer;
import zh.lingvo.persistence.xml.PersistenceManager;
import zh.lingvo.rest.Payload;
import zh.lingvo.rest.entities.word.WordRestEntity;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.util.ConfigReader;

import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@RestController
@ApiMapping
@RequestMapping("/api/words")
public class WordEditionController {
    private static final ConfigReader config = ConfigReader.get();

    @Value("${app.dictionaries.location}")
    private String dictionariesLocation;

    private DictionaryCache dictionaryCache;
    private LanguagesCache languagesCache;
    private Writer writer;

    public WordEditionController(DictionaryCache dictionaryCache, LanguagesCache languagesCache, PersistenceManager writer) {
        this.dictionaryCache = dictionaryCache;
        this.languagesCache = languagesCache;
        this.writer = writer;
    }

    @PutMapping("/{lang}/{id}/name")
    public WordRestEntity updateWordName(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @RequestBody Name name
    ) {
        return updateWord(languageCode, wordId, word -> word.setName(name));
    }

    @PostMapping("/{lang}/{id}/transcription")
    public WordRestEntity createTranscription(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @RequestBody String ipa
    ) {
        Consumer<Word> addTranscription = (word) -> {
            Transcription transcription = new Transcription(null, ipa);
            List<Transcription> transcriptions = CollectionUtils.getNotNull(word::getTranscriptions);
            List<Transcription> updatedTranscription = transcriptions == null
                    ? ImmutableList.of(transcription)
                    : ImmutableList.<Transcription>builder().addAll(transcriptions).add(transcription).build();
            word.setTranscriptions(updatedTranscription);
        };
        return updateWord(languageCode, wordId, addTranscription);
    }

    @PutMapping("/{lang}/{id}/transcription/{index}")
    public WordRestEntity updateTranscriptions(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("index") int index,
            @RequestBody String ipa
    ) {
        Consumer<Word> updateTranscription = word -> {
            Transcription transcription = new Transcription(null, ipa);
            List<Transcription> transcriptions = CollectionUtils.getNotNull(word::getTranscriptions);
            ImmutableList<Transcription> updatedTranscriptions = IntStream.range(0, transcriptions.size())
                    .mapToObj(i -> i == index ? transcription : transcriptions.get(i))
                    .collect(ImmutableList.toImmutableList());
            word.setTranscriptions(updatedTranscriptions);
        };
        return updateWord(languageCode, wordId, updateTranscription);
    }

    @DeleteMapping("/{lang}/{id}/transcription/{index}")
    public WordRestEntity deleteTranscription(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("index") int index
    ) {
        Consumer<Word> deleteTranscription = word -> {
            List<Transcription> transcriptions = CollectionUtils.getNotNull(word::getTranscriptions);
            ImmutableList<Transcription> updatedTranscriptions = IntStream.range(0, transcriptions.size())
                    .filter(i -> i != index)
                    .mapToObj(transcriptions::get)
                    .collect(ImmutableList.toImmutableList());
            word.setTranscriptions(updatedTranscriptions);
        };
        return updateWord(languageCode, wordId, deleteTranscription);
    }

    @PostMapping("/{lang}/{id}/semanticBlock")
    public WordRestEntity createSemanticBlock(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId
    ) {
        Consumer<Word> createSemanticBlock = word -> {
            List<SemanticBlock> semanticBlocks = CollectionUtils.getNotNull(word::getSemanticBlocks);
            List<SemanticBlock> updatedSemanticBlocks = ImmutableList.<SemanticBlock>builder()
                    .addAll(semanticBlocks)
                    .add(new SemanticBlock())
                    .build();
            word.setSemanticBlocks(updatedSemanticBlocks);
        };
        return updateWord(languageCode, wordId, createSemanticBlock);
    }

    @DeleteMapping("/{lang}/{id}/semanticBlock/{index}")
    public WordRestEntity deleteSemanticBlock(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("index") int index
    ) {
        Consumer<Word> deleteSemanticBlock = word -> {
            List<SemanticBlock> semanticBlocks = CollectionUtils.getNotNull(word::getSemanticBlocks);
            if (semanticBlocks.size() <= index) return;
            if (!CollectionUtils.getNotNull(semanticBlocks.get(index)::getPosBlocks).isEmpty()) return;
            List<SemanticBlock> updatedSemanticBlocks = IntStream.range(0, semanticBlocks.size())
                    .filter(i -> i != index)
                    .mapToObj(semanticBlocks::get)
                    .collect(ImmutableList.toImmutableList());
            word.setSemanticBlocks(updatedSemanticBlocks);
        };
        return updateWord(languageCode, wordId, deleteSemanticBlock);
    }

    @PostMapping("/{lang}/{id}/partOfSpeechBlock/{sbIndex}")
    public WordRestEntity createPartOfSpeechBlock(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int semanticBlockIndex,
            @RequestBody String partOfSpeech
    ) {
        Consumer<Word> addPartOfSpeechBlock = word -> {
            List<SemanticBlock> semanticBlocks = CollectionUtils.getNotNull(word::getSemanticBlocks);
            if (semanticBlockIndex >= semanticBlocks.size()) return;
            SemanticBlock semanticBlock = semanticBlocks.get(semanticBlockIndex);

            List<PosBlock> posBlocks = CollectionUtils.getNotNull(semanticBlock::getPosBlocks);
            PosBlock newPosBlock = new PosBlock(PartOfSpeech.fromName(languagesCache.get(languageCode), partOfSpeech));
            List<PosBlock> updatedPosBlocks = ImmutableList.<PosBlock>builder()
                    .addAll(posBlocks)
                    .add(newPosBlock)
                    .build();

            semanticBlock.setPosBlocks(updatedPosBlocks);
        };
        return updateWord(languageCode, wordId, addPartOfSpeechBlock);
    }

    @DeleteMapping("/{lang}/{id}/partOfSpeechBlock/{sbIndex}/{pos}")
    public WordRestEntity deletePartOfSpeech(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int semanticBlockIndex,
            @PathVariable("pos") String partOfSpeech
    ) {
        Consumer<Word> deletePos = word -> {
            List<SemanticBlock> semanticBlocks = CollectionUtils.getNotNull(word::getSemanticBlocks);
            if (semanticBlocks.size() <= semanticBlockIndex) return;

            SemanticBlock semanticBlock = semanticBlocks.get(semanticBlockIndex);
            List<PosBlock> posBlocks = CollectionUtils.getNotNull(semanticBlock::getPosBlocks);
            PartOfSpeech pos = PartOfSpeech.fromName(languagesCache.get(languageCode), partOfSpeech);
            PosBlock posBlock = posBlocks.stream()
                    .filter(pBlock -> Objects.equals(pBlock.getPos(), pos))
                    .findAny()
                    .orElse(null);
            if (posBlock == null) return;

            List<Meaning> meanings = CollectionUtils.getNotNull(posBlock::getMeanings);
            if (!meanings.isEmpty()) return;

            ImmutableList<PosBlock> updatedPartOfSpechBlocks = posBlocks.stream()
                    .filter(pBlock -> !Objects.equals(pBlock.getPos(), pos))
                    .collect(ImmutableList.toImmutableList());
            semanticBlock.setPosBlocks(updatedPartOfSpechBlocks);
        };
        return updateWord(languageCode, wordId, deletePos);
    }

    @PostMapping("/{lang}/{id}/meaning/{sbIndex}/{posIndex}")
    public WordRestEntity createMeaning(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int semanticBlockIndex,
            @PathVariable("posIndex") int posIndex,
            @RequestBody String translation
    ) {
        return updateWord(languageCode, wordId, word -> {
            PosBlock posBlock = getPosBlock(word, semanticBlockIndex, posIndex);
            if (posBlock == null) return;
            List<Meaning> updatedMeanings = ImmutableList.<Meaning>builder()
                    .addAll(CollectionUtils.getNotNull(posBlock::getMeanings))
                    .add(new Meaning(ImmutableList.of(new Translation(translation))))
                    .build();
            posBlock.setMeanings(updatedMeanings);
        });
    }

    @PutMapping("/{lang}/{id}/meaning/{sbIndex}/{posIndex}/remark/{mIndex}")
    public WordRestEntity editMeaningRemark(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int semanticBlockIndex,
            @PathVariable("posIndex") int posIndex,
            @PathVariable("mIndex") int mIndex,
            @RequestBody Payload payload
    ) {
        return updateWord(languageCode, wordId, word -> {
            PosBlock posBlock = getPosBlock(word, semanticBlockIndex, posIndex);
            if (posBlock == null) return;

            List<Meaning> meanings = CollectionUtils.getNotNull(posBlock::getMeanings);
            if (mIndex >= meanings.size()) return;

            String payloadData = payload.getData();
            String remark = Strings.isNullOrEmpty(payloadData) ? null : payloadData;
            Meaning meaning = meanings.get(mIndex);
            meaning.setRemark(remark);
            removeMeaningIfNecessary(meaning, posBlock, meanings, mIndex);
        });
    }

    @PutMapping("/{lang}/{id}/meaning/{sbIndex}/{posIndex}/{mIndex}/translation/{index}")
    public WordRestEntity editTranslation(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int semanticBlockIndex,
            @PathVariable("posIndex") int posIndex,
            @PathVariable("mIndex") int mIndex,
            @PathVariable("index") int trIndex,
            @RequestBody Payload payload
    ) {
        return updateWord(languageCode, wordId, word -> {
            editTranslation(word, semanticBlockIndex, posIndex, mIndex, trIndex,
                    translation -> translation.setTranslation(payload.getData()));
        });
    }

    @PutMapping("/{lang}/{id}/meaning/{sbIndex}/{posIndex}/{mIndex}/elaboration/{index}")
    public WordRestEntity editElaboration(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int semanticBlockIndex,
            @PathVariable("posIndex") int posIndex,
            @PathVariable("mIndex") int mIndex,
            @PathVariable("index") int trIndex,
            @RequestBody Payload payload
    ) {
        return updateWord(languageCode, wordId, word -> {
            editTranslation(word, semanticBlockIndex, posIndex, mIndex, trIndex,
                    translation -> translation.setElaboration(payload.getData()));
        });

    }

    @PutMapping("/{lang}/{id}/{sbIndex}/{posIndex}/{mIndex}/example/{index}/remark")
    public WordRestEntity editExampleRemark(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int sbIndex,
            @PathVariable("posIndex") int posIndex,
            @PathVariable("mIndex") int mIndex,
            @PathVariable("index") int exIndex,
            @RequestBody Payload payload
    ) {
        return updateWord(languageCode, wordId, word -> {
            editExample(word, sbIndex, posIndex, mIndex, exIndex,
                    example -> example.setRemark(payload.getData()));
        });
    }

    @PutMapping("/{lang}/{id}/{sbIndex}/{posIndex}/{mIndex}/example/{index}/explanation")
    public WordRestEntity editExampleExplanation(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int sbIndex,
            @PathVariable("posIndex") int posIndex,
            @PathVariable("mIndex") int mIndex,
            @PathVariable("index") int exIndex,
            @RequestBody Payload payload
    ) {
        return updateWord(languageCode, wordId, word -> {
            editExample(word, sbIndex, posIndex, mIndex, exIndex,
                    example -> example.setExplanation(payload.getData()));
        });
    }

    @PutMapping("/{lang}/{id}/{sbIndex}/{posIndex}/{mIndex}/example/{index}/expression")
    public WordRestEntity editExampleExpression(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("sbIndex") int sbIndex,
            @PathVariable("posIndex") int posIndex,
            @PathVariable("mIndex") int mIndex,
            @PathVariable("index") int exIndex,
            @RequestBody Payload payload
    ) {
        return updateWord(languageCode, wordId, word -> {
            editExample(word, sbIndex, posIndex, mIndex, exIndex,
                    example -> example.setExpression(payload.getData()));
        });
    }



    private void editTranslation(
            Word word,
            int semanticBlockIndex,
            int posIndex,
            int mIndex,
            int trIndex,
            Consumer<Translation> translationEditor
    ) {
        editInMeaning(word, semanticBlockIndex, posIndex, mIndex, trIndex,
                Meaning::getTranslations, Translation::new, translationEditor, Meaning::setTranslations);
    }

    private void editExample(
            Word word,
            int semanticBlockIndex,
            int posIndex,
            int mIndex,
            int exIndex,
            Consumer<Example> exampleEditor
    ) {
        editInMeaning(word, semanticBlockIndex, posIndex, mIndex, exIndex,
                Meaning::getExamples, Example::new, exampleEditor, Meaning::setExamples);
    }

    private <E extends WordEntity> void editInMeaning(
            Word word,
            int semanticBlockIndex,
            int posIndex,
            int mIndex,
            int index,
            Function<Meaning, List<E>> entityListSupplier,
            Supplier<E> newEntitySupplier,
            Consumer<E> entityEditor,
            BiConsumer<Meaning, List<E>> entityListSetter
    ) {
        PosBlock posBlock = getPosBlock(word, semanticBlockIndex, posIndex);
        if (posBlock == null) return;

        List<Meaning> meanings = CollectionUtils.getNotNull(posBlock::getMeanings);
        if (mIndex >= meanings.size()) return;

        Meaning meaning = meanings.get(mIndex);
        List<E> list = CollectionUtils.getNotNull(() -> entityListSupplier.apply(meaning));
        if (index > list.size()) return;

        if (index == list.size())
            list = CollectionUtils.add(list, newEntitySupplier.get());

        E entity = MoreObjects.firstNonNull(list.get(index), newEntitySupplier.get());
        entityEditor.accept(entity);
        entityListSetter.accept(meaning, list);
        removeWordEntityIfNecessary(entity, index, list, entities -> entityListSetter.accept(meaning, entities));
        removeMeaningIfNecessary(meaning, posBlock, meanings, mIndex);
    }

    private <E extends WordEntity> void removeWordEntityIfNecessary(
            E entity,
            int index,
            List<E> list,
            Consumer<List<E>> entityListSetter
    ) {
        if (entity.isVoid()) {
            List<E> updatedEntities = CollectionUtils.remove(list, index);
            entityListSetter.accept(updatedEntities);
        }
    }

    private void removeMeaningIfNecessary(Meaning meaning, PosBlock posBlock, List<Meaning> meanings, int mIndex) {
        if (meaning.isVoid()) {
            List<Meaning> updatedMeanings = CollectionUtils.remove(meanings, mIndex);
            posBlock.setMeanings(updatedMeanings);
        }
    }

    private PosBlock getPosBlock(Word word, int semanticBlockIndex, int posIndex) {
        List<SemanticBlock> semanticBlocks = CollectionUtils.getNotNull(word::getSemanticBlocks);
        if (semanticBlockIndex >= semanticBlocks.size()) return null;

        SemanticBlock semanticBlock = semanticBlocks.get(semanticBlockIndex);
        List<PosBlock> posBlocks = CollectionUtils.getNotNull(semanticBlock::getPosBlocks);
        if (posIndex >= posBlocks.size()) return null;

        return posBlocks.get(posIndex);
    }


    private WordRestEntity updateWord(String languageCode, UUID wordId, Consumer<Word> wordUpdater) {
        Dictionary dictionary = dictionaryCache.get(languageCode);
        Word word = dictionary.get(wordId);
        wordUpdater.accept(word);
        writer.saveDictionary(dictionary, getDictionaryLocation(languageCode));
        return new WordRestEntity(word, languagesCache.get(languageCode));
    }

    @NotNull
    private String getDictionaryLocation(String languageCode) {
        return Paths.get(dictionariesLocation).resolve(languageCode.toLowerCase() + "_dictionary.xml").toString();
    }
}
