package zh.lingvo.persistence.xml;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.words.Example;
import zh.lingvo.domain.words.Meaning;
import zh.lingvo.domain.words.PosBlock;
import zh.lingvo.domain.words.SemanticBlock;
import zh.lingvo.domain.words.Transcription;
import zh.lingvo.domain.words.Translation;
import zh.lingvo.domain.words.Word;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.persistence.xml.entities.DictionaryXmlEntity;
import zh.lingvo.persistence.xml.entities.word.ExampleXmlEntity;
import zh.lingvo.persistence.xml.entities.word.MeaningXmlEntity;
import zh.lingvo.persistence.xml.entities.word.PartOfSpeechBlockXmlEntity;
import zh.lingvo.persistence.xml.entities.word.SemanticBlockXmlEntity;
import zh.lingvo.persistence.xml.entities.word.TranslationXmlEntity;
import zh.lingvo.persistence.xml.entities.word.WordXmlEntity;
import zh.lingvo.persistence.PersistenceException;

import java.util.List;

@Service
public class XmlWordFactory {
    @Autowired
    private LanguagesCache languagesCache;

    XmlWordFactory() {
    }

    public Dictionary create(DictionaryXmlEntity xmlDictionary) {
        String languageCode = xmlDictionary.getLanguageCode();
        Language language = languagesCache.get().stream()
                .filter(lang -> lang.getCode().equals(languageCode))
                .findAny()
                .orElseThrow(() -> new PersistenceException(String.format("No language with code [%s] is found", languageCode)));

        Dictionary dictionary = new Dictionary(language);

        List<WordXmlEntity> xmlWords = xmlDictionary.getWords();
        if (xmlWords != null) xmlWords.stream()
                .map(this::create)
                .forEach(dictionary::add);

        return dictionary;
    }

    private Word create(WordXmlEntity xmlWord) {
        Word word = new Word(xmlWord.getId(), xmlWord.getWord());
        word.setTranscriptions(xmlWord.getTranscriptions()
                .stream()
                .map(transcription -> new Transcription(null, transcription))
                .collect(ImmutableList.toImmutableList()));

        List<SemanticBlock> semGroups = CollectionUtils.transform(xmlWord::getSemanticBlocks, this::getSemanticGroup);
        word.setSemanticBlocks(semGroups);

        return word;
    }

    private SemanticBlock getSemanticGroup(SemanticBlockXmlEntity xmlSemGroup) {
        SemanticBlock semGroup = new SemanticBlock();
        List<PosBlock> semBlocks = CollectionUtils.transform(xmlSemGroup::getPartOfSpeechBlocks, this::getSemanticBlock);
        semGroup.setPosBlocks(semBlocks);
        return semGroup;

    }

    private PosBlock getSemanticBlock(PartOfSpeechBlockXmlEntity xmlSemBlock) {
        PartOfSpeech partOfSpeech = xmlSemBlock.getPartOfSpeech();
        PosBlock semBlock = new PosBlock(partOfSpeech);
        List<Meaning> meanings = CollectionUtils.transform(xmlSemBlock::getMeanings, this::getMeaning);
        semBlock.setMeanings(meanings);
        return semBlock;
    }

    private Meaning getMeaning(MeaningXmlEntity xmlMeaning) {
        Meaning meaning = new Meaning();
        List<Translation> translations = CollectionUtils.transform(xmlMeaning::getTranslations, this::getTranslation);
        List<Example> examples = CollectionUtils.transform(xmlMeaning::getExamples, this::getExample);

        meaning.setRemark(xmlMeaning.getRemark());
        meaning.setTranslations(translations);
        meaning.setExamples(examples);

        return meaning;
    }

    private Translation getTranslation(TranslationXmlEntity xmlTranslation) {
        Translation translation = new Translation();
        translation.setTranslation(xmlTranslation.getTranslation());
        translation.setElaboration(xmlTranslation.getElaboration());
        return translation;
    }

    private Example getExample(ExampleXmlEntity xmlExample) {
        Example example = new Example();
        example.setRemark(xmlExample.getRemark());
        example.setExpression(xmlExample.getExpression());
        example.setExplanation(xmlExample.getExplanation());
        return example;
    }
}
