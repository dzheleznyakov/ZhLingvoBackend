package zh.lingvo.persistence.xml

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.caches.LanguagesCache
import zh.lingvo.domain.Dictionary
import zh.lingvo.domain.languages.English
import zh.lingvo.persistence.xml.entities.DictionaryXml
import zh.lingvo.persistence.xml.entities.ExampleXml
import zh.lingvo.persistence.xml.entities.FormExceptionXml
import zh.lingvo.persistence.xml.entities.MeaningXml
import zh.lingvo.persistence.xml.entities.NameXml
import zh.lingvo.persistence.xml.entities.PosBlockXml
import zh.lingvo.persistence.xml.entities.SemanticBlockXml
import zh.lingvo.persistence.xml.entities.TranscriptionXml
import zh.lingvo.persistence.xml.entities.TranslationXml
import zh.lingvo.persistence.xml.entities.WordXml

import static zh.lingvo.persistence.xml.Constants.emptyDictionary
import static zh.lingvo.persistence.xml.Constants.emptyDictionaryXml
import static zh.lingvo.persistence.xml.Constants.emptyExample
import static zh.lingvo.persistence.xml.Constants.emptyExampleXml
import static zh.lingvo.persistence.xml.Constants.emptyMeaningXml
import static zh.lingvo.persistence.xml.Constants.emptyPosBlock
import static zh.lingvo.persistence.xml.Constants.emptyPosBlockXml
import static zh.lingvo.persistence.xml.Constants.emptySemanticBlock
import static zh.lingvo.persistence.xml.Constants.emptySemanticBlockXml
import static zh.lingvo.persistence.xml.Constants.emptyWord
import static zh.lingvo.persistence.xml.Constants.emptyWordXml
import static zh.lingvo.persistence.xml.Constants.examplesOnlyMeaning
import static zh.lingvo.persistence.xml.Constants.examplesOnlyMeaningXml
import static zh.lingvo.persistence.xml.Constants.fullDictionary
import static zh.lingvo.persistence.xml.Constants.fullDictionaryXml
import static zh.lingvo.persistence.xml.Constants.fullExample
import static zh.lingvo.persistence.xml.Constants.fullExampleXml
import static zh.lingvo.persistence.xml.Constants.fullFormException
import static zh.lingvo.persistence.xml.Constants.fullFormExceptionXml
import static zh.lingvo.persistence.xml.Constants.fullMeaning
import static zh.lingvo.persistence.xml.Constants.fullMeaningXml
import static zh.lingvo.persistence.xml.Constants.fullName
import static zh.lingvo.persistence.xml.Constants.fullNameXml
import static zh.lingvo.persistence.xml.Constants.fullPosBlock
import static zh.lingvo.persistence.xml.Constants.fullPosBlockXml
import static zh.lingvo.persistence.xml.Constants.fullTranscription
import static zh.lingvo.persistence.xml.Constants.fullTranscriptionXml
import static zh.lingvo.persistence.xml.Constants.fullTranslation
import static zh.lingvo.persistence.xml.Constants.fullTranslationXml
import static zh.lingvo.persistence.xml.Constants.fullWord
import static zh.lingvo.persistence.xml.Constants.fullWordXml
import static zh.lingvo.persistence.xml.Constants.longSemanticBlock
import static zh.lingvo.persistence.xml.Constants.longSemanticBlockXml
import static zh.lingvo.persistence.xml.Constants.mediumExample
import static zh.lingvo.persistence.xml.Constants.mediumExampleXml
import static zh.lingvo.persistence.xml.Constants.mixedMeaning
import static zh.lingvo.persistence.xml.Constants.mixedMeaningXml
import static zh.lingvo.persistence.xml.Constants.nullContentMeaning
import static zh.lingvo.persistence.xml.Constants.remarkOnlyMeaning
import static zh.lingvo.persistence.xml.Constants.remarkOnlyMeaningXml
import static zh.lingvo.persistence.xml.Constants.shortExample
import static zh.lingvo.persistence.xml.Constants.shortExampleXml
import static zh.lingvo.persistence.xml.Constants.shortFormException
import static zh.lingvo.persistence.xml.Constants.shortFormExceptionXml
import static zh.lingvo.persistence.xml.Constants.shortName
import static zh.lingvo.persistence.xml.Constants.shortNameXml
import static zh.lingvo.persistence.xml.Constants.shortPosBlock
import static zh.lingvo.persistence.xml.Constants.shortPosBlockXml
import static zh.lingvo.persistence.xml.Constants.shortSemanticBlock
import static zh.lingvo.persistence.xml.Constants.shortSemanticBlockXml
import static zh.lingvo.persistence.xml.Constants.shortTranscription
import static zh.lingvo.persistence.xml.Constants.shortTranscriptionXml
import static zh.lingvo.persistence.xml.Constants.shortTranslation
import static zh.lingvo.persistence.xml.Constants.shortTranslationXml
import static zh.lingvo.persistence.xml.Constants.translationsOnlyMeaning
import static zh.lingvo.persistence.xml.Constants.translationsOnlyMeaningXml

class XmlReaderSpec extends Specification {
    private LanguagesCache languagesCache = [];
    private DictionaryFactory wordFactory = [languagesCache]
    private XmlReader reader = [wordFactory]

    @Unroll
    def "Reader can read entities: #expectedEntity.toString()"() {
        when: 'the entity is loaded'
        def entity = reader.loadEntity new ByteArrayInputStream(xml.bytes), clazz

        then: 'the value is read correctly'
        entity == expectedEntity

        where: 'the parameters are'
        xml                        | clazz            || expectedEntity
        shortTranscriptionXml      | TranscriptionXml || shortTranscription
        fullTranscriptionXml       | TranscriptionXml || fullTranscription
        fullTranslationXml         | TranslationXml   || fullTranslation
        shortTranslationXml        | TranslationXml   || shortTranslation
        fullExampleXml             | ExampleXml       || fullExample
        mediumExampleXml           | ExampleXml       || mediumExample
        shortExampleXml            | ExampleXml       || shortExample
        emptyExampleXml            | ExampleXml       || emptyExample
        fullNameXml                | NameXml          || fullName
        shortNameXml               | NameXml          || shortName
        fullFormExceptionXml       | FormExceptionXml || fullFormException
        shortFormExceptionXml      | FormExceptionXml || shortFormException
        emptyMeaningXml            | MeaningXml       || nullContentMeaning
        remarkOnlyMeaningXml       | MeaningXml       || remarkOnlyMeaning
        translationsOnlyMeaningXml | MeaningXml       || translationsOnlyMeaning
        examplesOnlyMeaningXml     | MeaningXml       || examplesOnlyMeaning
        mixedMeaningXml            | MeaningXml       || mixedMeaning
        fullMeaningXml             | MeaningXml       || fullMeaning
        emptyPosBlockXml           | PosBlockXml      || emptyPosBlock
        shortPosBlockXml           | PosBlockXml      || shortPosBlock
        fullPosBlockXml            | PosBlockXml      || fullPosBlock
        emptySemanticBlockXml      | SemanticBlockXml || emptySemanticBlock
        shortSemanticBlockXml      | SemanticBlockXml || shortSemanticBlock
        longSemanticBlockXml       | SemanticBlockXml || longSemanticBlock
        emptyWordXml               | WordXml          || emptyWord
        fullWordXml                | WordXml          || fullWord
        emptyDictionaryXml         | DictionaryXml    || emptyDictionary
        fullDictionaryXml          | DictionaryXml    || fullDictionary
    }

    def "Reader can load dictionary"() {
        when: "the dictionary file is read"
        def readDictionary = reader.loadDictionary('src/test/resources/xml/test_dictionary.xml')
        Dictionary expectedDictionary = new Dictionary(English.instance)
        expectedDictionary.add TestEntities.getBoxWord()
        expectedDictionary.add TestEntities.getManWord()

        then: 'the loaded dictionary is correct'
        readDictionary == expectedDictionary
    }

    def "If dictionary file does not exist, the reader returns null"() {
        when: 'the reader tries to load a dictionary from a non-existent file'
        def readDictionary = reader.loadDictionary('fake/path/dictionary.xml')

        then: 'the null is returned'
        readDictionary == null
    }
}
