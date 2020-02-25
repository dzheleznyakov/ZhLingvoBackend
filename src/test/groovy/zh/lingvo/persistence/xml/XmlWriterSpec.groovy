package zh.lingvo.persistence.xml

import spock.lang.Specification
import spock.lang.Unroll
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

import static zh.lingvo.persistence.xml.Constants.emptyContentMeaning
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

class XmlWriterSpec extends Specification {
    private DictionaryXmlFactory dictionaryXmlFactory = []
    private XmlWriter writer = [dictionaryXmlFactory]

    @Unroll
    def "Writer can write entities: #expectedXml"() {
        when: 'the entity is written'
        OutputStream out = new ByteArrayOutputStream()
        writer.toOutputStream(entity, out)

        then: 'then the resulting xml has the correct format'
        new String(out.toByteArray()) == expectedXml

        where: 'the parameters are'
        entity                  | clazz            || expectedXml
        fullTranscription       | TranscriptionXml || fullTranscriptionXml
        shortTranscription      | TranscriptionXml || shortTranscriptionXml
        fullTranslation         | TranslationXml   || fullTranslationXml
        shortTranslation        | TranslationXml   || shortTranslationXml
        fullExample             | ExampleXml       || fullExampleXml
        mediumExample           | ExampleXml       || mediumExampleXml
        shortExample            | ExampleXml       || shortExampleXml
        emptyExample            | ExampleXml       || emptyExampleXml
        shortName               | NameXml          || shortNameXml
        fullName                | NameXml          || fullNameXml
        fullFormException       | FormExceptionXml || fullFormExceptionXml
        shortFormException      | FormExceptionXml || shortFormExceptionXml
        nullContentMeaning      | MeaningXml       || emptyMeaningXml
        emptyContentMeaning     | MeaningXml       || emptyMeaningXml
        remarkOnlyMeaning       | MeaningXml       || remarkOnlyMeaningXml
        translationsOnlyMeaning | MeaningXml       || translationsOnlyMeaningXml
        examplesOnlyMeaning     | MeaningXml       || examplesOnlyMeaningXml
        mixedMeaning            | MeaningXml       || mixedMeaningXml
        fullMeaning             | MeaningXml       || fullMeaningXml
        emptyPosBlock           | PosBlockXml      || emptyPosBlockXml
        shortPosBlock           | PosBlockXml      || shortPosBlockXml
        fullPosBlock            | PosBlockXml      || fullPosBlockXml
        emptySemanticBlock      | SemanticBlockXml || emptySemanticBlockXml
        shortSemanticBlock      | SemanticBlockXml || shortSemanticBlockXml
        longSemanticBlock       | SemanticBlockXml || longSemanticBlockXml
        emptyWord               | WordXml          || emptyWordXml
        fullWord                | WordXml          || fullWordXml
        emptyDictionary         | DictionaryXml    || emptyDictionaryXml
        fullDictionary          | DictionaryXml    || fullDictionaryXml
    }

    def "Writer can save a dictionary"() {
        given: 'a dictionary'
        Dictionary dictionary = new Dictionary(English.instance)
        dictionary.add TestEntities.getManWord()
        dictionary.add TestEntities.getBoxWord()
        String filePath = 'temp-test-dictionary'

        expect: 'the dictionary file does not exist'
        File file = [filePath]
        !file.exists()

        when: "the dictionary file is saved"
        writer.saveDictionary(dictionary, filePath)
        def expectedFileContent = postProcessXml(getClass().classLoader.getResource('xml/test_dictionary.xml').text)
        def actualFileContent = postProcessXml(file.text)

        then: "the saved file content is as expected"
        actualFileContent == expectedFileContent

        cleanup: 'remove saved dictionary'
        if (file.exists()) file.delete()
    }

    private String postProcessXml(String xmlString) {
        def processedString = xmlString
                .replaceAll('>\\s+<', '><')
                .replaceAll('" />', '"/>')
                .trim()

        def manEntry
        def boxEntry

        def pattern = ~/.*(<word id="[\w\d\-]+"><name value="man"\/>(?:(?!<word).)*<\/word>).*/
        def matcher = pattern.matcher(processedString)
        if (matcher.matches()) {
            manEntry = matcher.group(1)
        } else {
            throw new IllegalArgumentException("The following string has wrong format: [$xmlString]")
        }

        pattern = ~/.*(<word id="[\w\d\-]+"><name value="box"\/>(?:(?!<word).)*<\/word>).*/
        matcher = pattern.matcher(processedString)
        if (matcher.matches()) {
            boxEntry = matcher.group(1)
        } else {
            throw new IllegalArgumentException("The following string has wrong format: [$xmlString]")
        }

        processedString = processedString.replace(manEntry, '')
        processedString = processedString.replaceAll(boxEntry, "$manEntry$boxEntry")


        return processedString
    }
}
