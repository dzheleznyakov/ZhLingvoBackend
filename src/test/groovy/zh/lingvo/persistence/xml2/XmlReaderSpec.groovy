package zh.lingvo.persistence.xml2

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.persistence.xml2.entities.DictionaryXml
import zh.lingvo.persistence.xml2.entities.ExampleXml
import zh.lingvo.persistence.xml2.entities.FormExceptionXml
import zh.lingvo.persistence.xml2.entities.MeaningXml
import zh.lingvo.persistence.xml2.entities.NameXml
import zh.lingvo.persistence.xml2.entities.PosBlockXml
import zh.lingvo.persistence.xml2.entities.SemanticBlockXml
import zh.lingvo.persistence.xml2.entities.TranscriptionXml
import zh.lingvo.persistence.xml2.entities.TranslationXml
import zh.lingvo.persistence.xml2.entities.WordXml

import static zh.lingvo.persistence.xml2.Constants.emptyDictionary
import static zh.lingvo.persistence.xml2.Constants.emptyDictionaryXml
import static zh.lingvo.persistence.xml2.Constants.emptyExample
import static zh.lingvo.persistence.xml2.Constants.emptyExampleXml
import static zh.lingvo.persistence.xml2.Constants.emptyMeaningXml
import static zh.lingvo.persistence.xml2.Constants.emptyPosBlock
import static zh.lingvo.persistence.xml2.Constants.emptyPosBlockXml
import static zh.lingvo.persistence.xml2.Constants.emptySemanticBlock
import static zh.lingvo.persistence.xml2.Constants.emptySemanticBlockXml
import static zh.lingvo.persistence.xml2.Constants.emptyWord
import static zh.lingvo.persistence.xml2.Constants.emptyWordXml
import static zh.lingvo.persistence.xml2.Constants.examplesOnlyMeaning
import static zh.lingvo.persistence.xml2.Constants.examplesOnlyMeaningXml
import static zh.lingvo.persistence.xml2.Constants.fullDictionary
import static zh.lingvo.persistence.xml2.Constants.fullDictionaryXml
import static zh.lingvo.persistence.xml2.Constants.fullExample
import static zh.lingvo.persistence.xml2.Constants.fullExampleXml
import static zh.lingvo.persistence.xml2.Constants.fullFormException
import static zh.lingvo.persistence.xml2.Constants.fullFormExceptionXml
import static zh.lingvo.persistence.xml2.Constants.fullMeaning
import static zh.lingvo.persistence.xml2.Constants.fullMeaningXml
import static zh.lingvo.persistence.xml2.Constants.fullName
import static zh.lingvo.persistence.xml2.Constants.fullNameXml
import static zh.lingvo.persistence.xml2.Constants.fullPosBlock
import static zh.lingvo.persistence.xml2.Constants.fullPosBlockXml
import static zh.lingvo.persistence.xml2.Constants.fullTranscription
import static zh.lingvo.persistence.xml2.Constants.fullTranscriptionXml
import static zh.lingvo.persistence.xml2.Constants.fullTranslation
import static zh.lingvo.persistence.xml2.Constants.fullTranslationXml
import static zh.lingvo.persistence.xml2.Constants.fullWord
import static zh.lingvo.persistence.xml2.Constants.fullWordXml
import static zh.lingvo.persistence.xml2.Constants.longSemanticBlock
import static zh.lingvo.persistence.xml2.Constants.longSemanticBlockXml
import static zh.lingvo.persistence.xml2.Constants.mediumExample
import static zh.lingvo.persistence.xml2.Constants.mediumExampleXml
import static zh.lingvo.persistence.xml2.Constants.mixedMeaning
import static zh.lingvo.persistence.xml2.Constants.mixedMeaningXml
import static zh.lingvo.persistence.xml2.Constants.nullContentMeaning
import static zh.lingvo.persistence.xml2.Constants.shortExample
import static zh.lingvo.persistence.xml2.Constants.shortExampleXml
import static zh.lingvo.persistence.xml2.Constants.shortFormException
import static zh.lingvo.persistence.xml2.Constants.shortFormExceptionXml
import static zh.lingvo.persistence.xml2.Constants.shortName
import static zh.lingvo.persistence.xml2.Constants.shortNameXml
import static zh.lingvo.persistence.xml2.Constants.shortPosBlock
import static zh.lingvo.persistence.xml2.Constants.shortPosBlockXml
import static zh.lingvo.persistence.xml2.Constants.shortSemanticBlock
import static zh.lingvo.persistence.xml2.Constants.shortSemanticBlockXml
import static zh.lingvo.persistence.xml2.Constants.shortTranscription
import static zh.lingvo.persistence.xml2.Constants.shortTranscriptionXml
import static zh.lingvo.persistence.xml2.Constants.shortTranslation
import static zh.lingvo.persistence.xml2.Constants.shortTranslationXml
import static zh.lingvo.persistence.xml2.Constants.translationsOnlyMeaning
import static zh.lingvo.persistence.xml2.Constants.translationsOnlyMeaningXml

class XmlReaderSpec extends Specification {
    private XmlReader reader = []

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
}
