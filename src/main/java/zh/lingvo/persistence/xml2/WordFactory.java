package zh.lingvo.persistence.xml2;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Service;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.LinguisticCategory;
import zh.lingvo.domain.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.forms.NounWordForm;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.words.Example;
import zh.lingvo.domain.words.Meaning;
import zh.lingvo.domain.words.Name;
import zh.lingvo.domain.words.PosBlock;
import zh.lingvo.domain.words.SemanticBlock;
import zh.lingvo.domain.words.Transcription;
import zh.lingvo.domain.words.Translation;
import zh.lingvo.domain.words.Word;
import zh.lingvo.persistence.PersistenceException;
import zh.lingvo.persistence.xml2.entities.DictionaryXml;
import zh.lingvo.persistence.xml2.entities.ExampleXml;
import zh.lingvo.persistence.xml2.entities.FormExceptionXml;
import zh.lingvo.persistence.xml2.entities.MeaningXml;
import zh.lingvo.persistence.xml2.entities.NameXml;
import zh.lingvo.persistence.xml2.entities.PosBlockXml;
import zh.lingvo.persistence.xml2.entities.SemanticBlockXml;
import zh.lingvo.persistence.xml2.entities.TranscriptionXml;
import zh.lingvo.persistence.xml2.entities.TranslationXml;
import zh.lingvo.persistence.xml2.entities.WordXml;
import zh.lingvo.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

@Service
public class WordFactory {
    private LanguagesCache languagesCache;

    public WordFactory(LanguagesCache languagesCache) {
        this.languagesCache = languagesCache;
    }

    public Dictionary getDictionary(DictionaryXml dictionaryXml) {
        String langCode = dictionaryXml.getLang();
        Language language = languagesCache.get(langCode);
        Dictionary dictionary = new Dictionary(language);
        getWords(dictionaryXml).forEach(dictionary::add);
        return dictionary;
    }

    private Set<Word> getWords(DictionaryXml dictionaryXml) {
        return CollectionUtils.getNotNull(dictionaryXml::getWords).stream()
                .map(this::getWord)
                .collect(ImmutableSet.toImmutableSet());
    }

    private Word getWord(WordXml wordXml) {
        String name = wordXml.getName().getValue();

        String id = wordXml.getId();
        if (!id.startsWith("uuid"))
            throw new PersistenceException(String.format("Illegal word id format: [%s]", id));
        UUID wordId = UUID.fromString(id.substring("uuid".length()));

        List<Transcription> transcriptions = CollectionUtils.transform(wordXml::getTranscriptions, this::getTranscription);
        List<SemanticBlock> semanticBlocks = CollectionUtils.transform(wordXml::getSemanticBlocks, this::getSemanticBlock);
        Map<PartOfSpeech, List<Name>> formExceptions = CollectionUtils.transformToMap(
                wordXml::getFormExceptions,
                fe -> PartOfSpeech.valueOf(fe.getPos()),
                this::getFormExceptionNames);

        Word word = new Word(wordId, name);
        word.setTranscriptions(transcriptions);
        word.setSemanticBlocks(semanticBlocks);
        word.setFormExceptions(formExceptions);
        return word;
    }

    private Transcription getTranscription(TranscriptionXml transcriptionXml) {
        return new Transcription(transcriptionXml.getRemark(), transcriptionXml.getIpa());
    }

    private SemanticBlock getSemanticBlock(SemanticBlockXml semanticBlockXml) {
        List<PosBlock> posBlocks = CollectionUtils.transform(semanticBlockXml::getPosBlocks, this::getPosBlock);

        SemanticBlock semanticBlock = new SemanticBlock();
        semanticBlock.setPosBlocks(posBlocks);
        return semanticBlock;
    }

    private PosBlock getPosBlock(PosBlockXml posBlockXml) {
        String posName = posBlockXml.getPos();
        PartOfSpeech pos = PartOfSpeech.valueOf(posName);

        List<Meaning> meanings = CollectionUtils.transform(posBlockXml::getMeanings, this::getMeanings);

        PosBlock posBlock = new PosBlock(pos);
        posBlock.setMeanings(meanings);

        return posBlock;
    }

    private Meaning getMeanings(MeaningXml meaningXml) {
        String remark = meaningXml.getRemark();
        List<Translation> translations = CollectionUtils.transform(meaningXml::getTranslations, this::getTranslation);
        List<Example> examples = CollectionUtils.transform(meaningXml::getExamples, this::getExample);

        Meaning meaning = new Meaning();
        meaning.setRemark(remark);
        meaning.setTranslations(translations);
        meaning.setExamples(examples);
        return meaning;
    }

    private Translation getTranslation(TranslationXml translationXml) {
        Translation translation = new Translation(translationXml.getValue());
        translation.setElaboration(translationXml.getElaboration());
        return translation;
    }

    private Example getExample(ExampleXml exampleXml) {
        String expression = exampleXml.getExpression();
        String explanation = exampleXml.getExplanation();
        String remark = exampleXml.getRemark();
        Example example = new Example();
        example.setExpression(expression);
        example.setExplanation(explanation);
        example.setRemark(remark);
        return example;
    }

    private List<Name> getFormExceptionNames(FormExceptionXml formExceptionXml) {
        Function<NameXml, Name> getName = nameXml -> {
            String formString = nameXml.getForm();
            String[] formTokens = formString.split(" ");
            LinguisticCategory[] form = Arrays.stream(formTokens)
                    .map(formPart -> formPart.split("\\."))
                    .map(formPart -> getLinguisticCategory(formPart[0], formPart[1]))
                    .collect(ImmutableList.toImmutableList())
                    .toArray(new LinguisticCategory[formTokens.length]);

            Name name = new Name();
            name.setValue(nameXml.getValue());
            name.setForm(form);
            return name;
        };
        return CollectionUtils.transform(formExceptionXml::getNames, getName);
    }

    private LinguisticCategory getLinguisticCategory(String clazz, String name) {
        if (Objects.equals(Number.class.getSimpleName(), clazz))
            return Number.valueOf(name);
        if (Objects.equals(NounWordForm.class.getSimpleName(), clazz))
            return NounWordForm.valueOf(name);
        throw new IllegalArgumentException(String.format("Unknown category class [%s]", clazz));
    }
}
