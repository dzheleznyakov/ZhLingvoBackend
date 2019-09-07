package zh.lingvo.controllers;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.ApiMapping;
import zh.lingvo.caches.DictionaryCache;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.words.Meaning;
import zh.lingvo.domain.words.PartOfSpeechBlock;
import zh.lingvo.domain.words.SemanticBlock;
import zh.lingvo.domain.words.Translation;
import zh.lingvo.domain.words.Word;
import zh.lingvo.persistence.xml.XmlWriter;
import zh.lingvo.rest.entities.word.WordRestEntity;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.util.ConfigReader;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@RestController
@ApiMapping
@RequestMapping("/api/words")
public class WordEditionController {
    private static final ConfigReader config = ConfigReader.get();
    private static final String dictionariesLocation = config.getString("dictionariesLocation");

    private DictionaryCache dictionaryCache;
    private LanguagesCache languagesCache;
    private XmlWriter writer;

    public WordEditionController(DictionaryCache dictionaryCache, LanguagesCache languagesCache, XmlWriter writer) {
        this.dictionaryCache = dictionaryCache;
        this.languagesCache = languagesCache;
        this.writer = writer;
    }

    @PutMapping("/{lang}/{id}/name")
    public WordRestEntity updateWordName(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @RequestBody String name
    ) {
        return updateWord(languageCode, wordId, word -> word.setName(name));
    }

    @PostMapping("/{lang}/{id}/transcription")
    public WordRestEntity createTranscription(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @RequestBody String transcription
    ) {
        Consumer<Word> addTranscription = (word) -> {
            List<String> transcriptions = CollectionUtils.getNotNull(word::getTranscriptions);
            List<String> updatedTranscription = transcriptions == null
                    ? ImmutableList.of(transcription)
                    : ImmutableList.<String>builder().addAll(transcriptions).add(transcription).build();
            word.setTranscriptions(updatedTranscription);
        };
        return updateWord(languageCode, wordId, addTranscription);
    }

    @PutMapping("/{lang}/{id}/transcription/{index}")
    public WordRestEntity updateTranscriptions(
            @PathVariable("lang") String languageCode,
            @PathVariable("id") UUID wordId,
            @PathVariable("index") int index,
            @RequestBody String transcription
    ) {
        Consumer<Word> updateTranscription = word -> {
            List<String> transcriptions = CollectionUtils.getNotNull(word::getTranscriptions);
            ImmutableList<String> updatedTranscriptions = IntStream.range(0, transcriptions.size())
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
            List<String> transcriptions = CollectionUtils.getNotNull(word::getTranscriptions);
            ImmutableList<String> updatedTranscriptions = IntStream.range(0, transcriptions.size())
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
            if (!CollectionUtils.getNotNull(semanticBlocks.get(index)::getPartOfSpeechBlocks).isEmpty()) return;
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

            List<PartOfSpeechBlock> posBlocks = CollectionUtils.getNotNull(semanticBlock::getPartOfSpeechBlocks);
            PartOfSpeechBlock newPosBlock = new PartOfSpeechBlock(PartOfSpeech.fromName(languageCode, partOfSpeech));
            List<PartOfSpeechBlock> updatedPosBlocks = ImmutableList.<PartOfSpeechBlock>builder()
                    .addAll(posBlocks)
                    .add(newPosBlock)
                    .build();

            semanticBlock.setPartOfSpeechBlocks(updatedPosBlocks);
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
            List<PartOfSpeechBlock> partOfSpeechBlocks = CollectionUtils.getNotNull(semanticBlock::getPartOfSpeechBlocks);
            PartOfSpeech pos = PartOfSpeech.fromName(languageCode, partOfSpeech);
            PartOfSpeechBlock partOfSpeechBlock = partOfSpeechBlocks.stream()
                    .filter(posBlock -> Objects.equals(posBlock.getPartOfSpeech(), pos))
                    .findAny()
                    .orElse(null);
            if (partOfSpeechBlock == null) return;

            List<Meaning> meanings = CollectionUtils.getNotNull(partOfSpeechBlock::getMeanings);
            if (!meanings.isEmpty()) return;

            ImmutableList<PartOfSpeechBlock> updatedPartOfSpechBlocks = partOfSpeechBlocks.stream()
                    .filter(posBlock -> !Objects.equals(posBlock.getPartOfSpeech(), pos))
                    .collect(ImmutableList.toImmutableList());
            semanticBlock.setPartOfSpeechBlocks(updatedPartOfSpechBlocks);
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
            PartOfSpeechBlock posBlock = getPosBlock(word, semanticBlockIndex, posIndex);
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
            @RequestBody Map<String, String> payload
    ) {
        return updateWord(languageCode, wordId, word -> {
            PartOfSpeechBlock posBlock = getPosBlock(word, semanticBlockIndex, posIndex);
            if (posBlock == null) return;

            List<Meaning> meanings = CollectionUtils.getNotNull(posBlock::getMeanings);
            if (mIndex >= meanings.size()) return;

            String remark = payload.get("data");
            Meaning meaning = meanings.get(mIndex);
            meaning.setRemark(remark);
            removeMeaningIfNecessary(meaning, posBlock, meanings, mIndex);
        });
    }

    private void removeMeaningIfNecessary(Meaning meaning, PartOfSpeechBlock posBlock, List<Meaning> meanings, int mIndex) {
        if (
                Strings.isNullOrEmpty(meaning.getRemark())
                && CollectionUtils.getNotNull(meaning::getTranslations).isEmpty()
                && CollectionUtils.getNotNull(meaning::getExamples).isEmpty()
        ) {
            List<Meaning> updatedMeanings = IntStream.range(0, meanings.size())
                    .filter(i -> i != mIndex)
                    .mapToObj(meanings::get)
                    .collect(ImmutableList.toImmutableList());
            posBlock.setMeanings(updatedMeanings);
        }
    }

    private PartOfSpeechBlock getPosBlock(Word word, int semanticBlockIndex, int posIndex) {
        List<SemanticBlock> semanticBlocks = CollectionUtils.getNotNull(word::getSemanticBlocks);
        if (semanticBlockIndex >= semanticBlocks.size()) return null;

        SemanticBlock semanticBlock = semanticBlocks.get(semanticBlockIndex);
        List<PartOfSpeechBlock> posBlocks = CollectionUtils.getNotNull(semanticBlock::getPartOfSpeechBlocks);
        if (posIndex >= posBlocks.size()) return null;

        return posBlocks.get(posIndex);
    }

    @Nullable
    private List<Meaning> getMeanings(Word word, int semanticBlockIndex, int posIndex, int mIndex) {
        PartOfSpeechBlock posBlock = getPosBlock(word, semanticBlockIndex, posIndex);
        if (posBlock == null) return null;

        List<Meaning> meanings = CollectionUtils.getNotNull(posBlock::getMeanings);
        if (mIndex >= meanings.size()) return null;

        return meanings;
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
        return dictionariesLocation + languageCode.toLowerCase() + "_dictionary.xml";
    }
}
