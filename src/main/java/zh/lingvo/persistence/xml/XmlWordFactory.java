package zh.lingvo.persistence.xml;

import zh.lingvo.caches.LanguagesCache;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.domain.Language;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.words.Example;
import zh.lingvo.domain.words.Meaning;
import zh.lingvo.domain.words.SemanticBlock;
import zh.lingvo.domain.words.SemanticGroup;
import zh.lingvo.domain.words.Translation;
import zh.lingvo.domain.words.Word;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.persistence.xml.entities.DictionaryXmlEntity;
import zh.lingvo.persistence.xml.entities.word.ExampleXmlEntity;
import zh.lingvo.persistence.xml.entities.word.MeaningXmlEntity;
import zh.lingvo.persistence.xml.entities.word.SemanticBlockXmlEntity;
import zh.lingvo.persistence.xml.entities.word.SemanticGroupXmlEntity;
import zh.lingvo.persistence.xml.entities.word.TranslationXmlEntity;
import zh.lingvo.persistence.xml.entities.word.WordXmlEntity;
import zh.lingvo.persistence.PersistenceException;

import java.util.List;

public class XmlWordFactory {
    private XmlWordFactory() {
    }

    public static Dictionary create(DictionaryXmlEntity xmlDictionary) {
        String languageCode = xmlDictionary.getLanguageCode();
        Language language = LanguagesCache.get().stream()
                .filter(lang -> lang.getCode().equals(languageCode))
                .findAny()
                .orElseThrow(() -> new PersistenceException(String.format("No language with code [%s] is found", languageCode)));

        Dictionary dictionary = new Dictionary(language);

        List<WordXmlEntity> xmlWords = xmlDictionary.getWords();
        if (xmlWords != null) xmlWords.stream()
                .map(XmlWordFactory::create)
                .forEach(dictionary::add);

        return dictionary;
    }

    private static Word create(WordXmlEntity xmlWord) {
        Word word = new Word(xmlWord.getId(), xmlWord.getWord());
        word.setTranscriptions(xmlWord.getTranscriptions());

        List<SemanticGroup> semGroups = CollectionUtils.transform(xmlWord::getSemanticGroups, XmlWordFactory::getSemanticGroup);
        word.setSemanticGroups(semGroups);

        return word;
    }

    private static SemanticGroup getSemanticGroup(SemanticGroupXmlEntity xmlSemGroup) {
        SemanticGroup semGroup = new SemanticGroup();
        List<SemanticBlock> semBlocks = CollectionUtils.transform(xmlSemGroup::getSemanticBlocks, XmlWordFactory::getSemanticBlock);
        semGroup.setSemanticBlocks(semBlocks);
        return semGroup;

    }

    private static SemanticBlock getSemanticBlock(SemanticBlockXmlEntity xmlSemBlock) {
        PartOfSpeech partOfSpeech = xmlSemBlock.getPartOfSpeech();
        SemanticBlock semBlock = new SemanticBlock(partOfSpeech);
        List<Meaning> meanings = CollectionUtils.transform(xmlSemBlock::getMeanings, XmlWordFactory::getMeaning);
        semBlock.setMeanings(meanings);
        return semBlock;
    }

    private static Meaning getMeaning(MeaningXmlEntity xmlMeaning) {
        Meaning meaning = new Meaning();
        List<Translation> translations = CollectionUtils.transform(xmlMeaning::getTranslations, XmlWordFactory::getTranslation);
        List<Example> examples = CollectionUtils.transform(xmlMeaning::getExamples, XmlWordFactory::getExample);

        meaning.setRemark(xmlMeaning.getRemark());
        meaning.setTranslations(translations);
        meaning.setExamples(examples);

        return meaning;
    }

    private static Translation getTranslation(TranslationXmlEntity xmlTranslation) {
        Translation translation = new Translation();
        translation.setTranslation(xmlTranslation.getTranslation());
        translation.setElaboration(xmlTranslation.getElaboration());
        return translation;
    }

    private static Example getExample(ExampleXmlEntity xmlExample) {
        Example example = new Example();
        example.setRemark(xmlExample.getRemark());
        example.setExpression(xmlExample.getExpression());
        example.setExplanation(xmlExample.getExplanation());
        return example;
    }
}
