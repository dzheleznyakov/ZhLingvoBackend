package zh.lingvo.rest.entities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.words.Example;
import zh.lingvo.domain.words.Meaning;
import zh.lingvo.domain.words.PosBlock;
import zh.lingvo.domain.words.SemanticBlock;
import zh.lingvo.domain.words.Transcription;
import zh.lingvo.domain.words.Translation;
import zh.lingvo.domain.words.Word;
import zh.lingvo.rest.entities.word.ExampleRestEntity;
import zh.lingvo.rest.entities.word.MeaningRestEntity;
import zh.lingvo.rest.entities.word.SemanticBlockRestEntity;
import zh.lingvo.rest.entities.word.TranslationRestEntity;
import zh.lingvo.rest.entities.word.WordRestEntity;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.util.ConfigReader;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class JsonWordFactory {
    private static final ConfigReader config = ConfigReader.get();

    private static final Map<String, Map<String, PartOfSpeech>> posMap;
    static {
        Function<ConfigReader, Object[]> posMapFunc = conf -> new Object[]{
                conf.getString("name"),
                conf.getEnum("type", PartOfSpeech.class)
        };
        Function<ConfigReader, Map<String, PartOfSpeech>> langPosMapFunc = conf ->
                conf.getList("partsOfSpeeches", posMapFunc).stream()
                        .collect(ImmutableMap.toImmutableMap(arr -> (String)arr[0], arr -> (PartOfSpeech)arr[1]));
        posMap = config.getMap("languages", c -> c.getString("code"), langPosMapFunc);
    }

    private JsonWordFactory() {
    }

    public static Word getWord(WordRestEntity jsonWord, String languageCode) {
        Word word = new Word(UUID.fromString(jsonWord.getId()), jsonWord.getWord());
        word.setTranscriptions(jsonWord.getTranscriptions()
                .stream()
                .map(transcription -> new Transcription(null, transcription))
                .collect(ImmutableList.toImmutableList()));

        List<SemanticBlock> semanticBlocks = CollectionUtils.transform(jsonWord::getSemanticBlocks, jsonSemBlocks -> getSemanticGroup(jsonSemBlocks, languageCode));
        word.setSemanticBlocks(semanticBlocks);

        return word;
    }

    private static SemanticBlock getSemanticGroup(List<SemanticBlockRestEntity> jsonSemanticBlocks, String languageCode) {
        SemanticBlock semanticBlock = new SemanticBlock();
        List<PosBlock> posBlocks = CollectionUtils.transform(() -> jsonSemanticBlocks, jsonSemBlock -> getSemanticBlock(jsonSemBlock, languageCode));
        semanticBlock.setPosBlocks(posBlocks);
        return semanticBlock;
    }

    private static PosBlock getSemanticBlock(SemanticBlockRestEntity jsonSemBlock, String languageCode) {
        String posName = jsonSemBlock.getType();
        PartOfSpeech partOfSpeech = posMap.get(languageCode).get(posName);
        PosBlock semBlock = new PosBlock(partOfSpeech);
        List<Meaning> meanings = CollectionUtils.transform(jsonSemBlock::getMeanings, JsonWordFactory::getMeaning);
        semBlock.setMeanings(meanings);
        return semBlock;
    }

    private static Meaning getMeaning(MeaningRestEntity jsonMeaning) {
        Meaning meaning = new Meaning();
        List<Translation> translations = CollectionUtils.transform(jsonMeaning::getTranslations, JsonWordFactory::getTranslation);
        List<Example> examples = CollectionUtils.transform(jsonMeaning::getExamples, JsonWordFactory::getExample);

        meaning.setRemark(jsonMeaning.getRemark());
        meaning.setTranslations(translations);
        meaning.setExamples(examples);

        return meaning;
    }

    private static Translation getTranslation(TranslationRestEntity jsonTranslation) {
        Translation translation = new Translation();
        translation.setTranslation(jsonTranslation.getTranslation());
        translation.setElaboration(jsonTranslation.getElaboration());
        return translation;
    }

    private static Example getExample(ExampleRestEntity jsonExample) {
        Example example = new Example();
        example.setRemark(jsonExample.getRemark());
        example.setExpression(jsonExample.getExpression());
        example.setExplanation(jsonExample.getExplanation());
        return example;
//        return new GS
    }
}
