package zh.lingvo.persistence.xml;

import org.springframework.stereotype.Service;
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
import zh.lingvo.persistence.xml.entities.DictionaryXml;
import zh.lingvo.persistence.xml.entities.ExampleXml;
import zh.lingvo.persistence.xml.entities.FormExceptionXml;
import zh.lingvo.persistence.xml.entities.MeaningXml;
import zh.lingvo.persistence.xml.entities.NameXml;
import zh.lingvo.persistence.xml.entities.PosBlockXml;
import zh.lingvo.persistence.xml.entities.SemanticBlockXml;
import zh.lingvo.persistence.xml.entities.TranscriptionXml;
import zh.lingvo.persistence.xml.entities.TranslationXml;
import zh.lingvo.persistence.xml.entities.WordXml;
import zh.lingvo.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryXmlFactory {

    public DictionaryXml getDictionaryXml(Dictionary dictionary) {
        String lang = dictionary.getLanguage().getCode();
        List<WordXml> wordsXml = CollectionUtils.transform(dictionary::getWords, this::getWordXml);

        DictionaryXml dictionaryXml = new DictionaryXml();
        dictionaryXml.setLang(lang);
        dictionaryXml.setWords(wordsXml);

        return dictionaryXml;
    }

    private WordXml getWordXml(Word word) {
        String id = "uuid" + word.getId();

        NameXml nameXml = new NameXml();
        nameXml.setValue(word.getName().getValue());

        List<TranscriptionXml> transcriptionsXml = CollectionUtils.transform(word::getTranscriptions, this::getTranscriptionXml);
        List<SemanticBlockXml> semanticBlocksXml = CollectionUtils.transform(word::getSemanticBlocks, this::getSemanticBlockXml);
        List<FormExceptionXml> formExceptionsXml = CollectionUtils.transformToList(word::getFormExceptions, this::getFormExceptionXml);

        WordXml wordXml = new WordXml();
        wordXml.setId(id);
        wordXml.setName(nameXml);
        wordXml.setTranscriptions(transcriptionsXml);
        wordXml.setSemanticBlocks(semanticBlocksXml);
        wordXml.setFormExceptions(formExceptionsXml);

        return wordXml;
    }

    private TranscriptionXml getTranscriptionXml(Transcription transcription) {
        TranscriptionXml transcriptionXml = new TranscriptionXml();
        transcriptionXml.setIpa(transcription.getIpa());
        transcriptionXml.setRemark(transcription.getRemark());
        return transcriptionXml;
    }


    private SemanticBlockXml getSemanticBlockXml(SemanticBlock semanticBlock) {
        List<PosBlockXml> posBlocksXml = CollectionUtils.transform(semanticBlock::getPosBlocks, this::getPosBlockXml);

        SemanticBlockXml semanticBlockXml = new SemanticBlockXml();
        semanticBlockXml.setPosBlocks(posBlocksXml);
        return semanticBlockXml;
    }

    private PosBlockXml getPosBlockXml(PosBlock posBlock) {
        String posName = posBlock.getPos().name();
        List<MeaningXml> meaningsXml = CollectionUtils.transform(posBlock::getMeanings, this::getMeaningXml);

        PosBlockXml posBlockXml = new PosBlockXml();
        posBlockXml.setPos(posName);
        posBlockXml.setMeanings(meaningsXml);
        return posBlockXml;
    }

    private MeaningXml getMeaningXml(Meaning meaning) {
        String remark = meaning.getRemark();
        List<TranslationXml> translationsXml = CollectionUtils.transform(meaning::getTranslations, this::getTranslationXml);
        List<ExampleXml> examplesXml = CollectionUtils.transform(meaning::getExamples, this::getExampleXml);

        MeaningXml meaningXml = new MeaningXml();
        meaningXml.setRemark(remark);
        meaningXml.setTranslations(translationsXml);
        meaningXml.setExamples(examplesXml);
        return meaningXml;
    }

    private TranslationXml getTranslationXml(Translation translation) {
        TranslationXml translationXml = new TranslationXml();
        translationXml.setValue(translation.getTranslation());
        translationXml.setElaboration(translation.getElaboration());
        return translationXml;
    }

    private ExampleXml getExampleXml(Example example) {
        ExampleXml exampleXml = new ExampleXml();
        exampleXml.setRemark(example.getRemark());
        exampleXml.setExpression(example.getExpression());
        exampleXml.setExplanation(example.getExplanation());
        return exampleXml;
    }

    private FormExceptionXml getFormExceptionXml(PartOfSpeech pos, List<Name> names) {
        String posName = pos.name();
        List<NameXml> namesXml = CollectionUtils.transform(() -> names, this::getNameXml);

        FormExceptionXml formExceptionXml = new FormExceptionXml();
        formExceptionXml.setPos(posName);
        formExceptionXml.setNames(namesXml);
        return formExceptionXml;
    }

    private NameXml getNameXml(Name name) {
        String value = name.getValue();
        String form = Arrays.stream(name.getForm())
                .map(linguisticCategory -> linguisticCategory.getClass().getSimpleName() + "." + linguisticCategory.name())
                .collect(Collectors.joining(" "));

        NameXml nameXml = new NameXml();
        nameXml.setValue(value);
        nameXml.setForm(form);
        return nameXml;
    }
}
