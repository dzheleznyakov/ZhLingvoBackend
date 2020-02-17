package zh.lingvo.persistence.xml


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

interface Constants {
    static TranscriptionXml fullTranscription = new TranscriptionXml(remark: 'fullTestRemark', ipa: 'fullTestIpa')
    static TranscriptionXml shortTranscription = new TranscriptionXml(ipa: 'shortTestIpa')
    static String fullTranscriptionXml = '<transcription remark="fullTestRemark" ipa="fullTestIpa"/>'
    static String shortTranscriptionXml = '<transcription ipa="shortTestIpa"/>'

    static TranslationXml fullTranslation = new TranslationXml(value: 'fullTestValue', elaboration: 'fullTestElaboration')
    static TranslationXml shortTranslation = new TranslationXml(value: 'shortTestValue')
    static String fullTranslationXml = '<translation value="fullTestValue" elaboration="fullTestElaboration"/>'
    static String shortTranslationXml = '<translation value="shortTestValue"/>'

    static ExampleXml fullExample = new ExampleXml(expression: 'fullTestExpression', explanation: 'fullTestExplanation', remark: 'fullTestRemark')
    static ExampleXml mediumExample = new ExampleXml(expression: 'mediumTestExpression', explanation: 'mediumTestExplanation')
    static ExampleXml shortExample = new ExampleXml(expression: 'shortTestExpression')
    static ExampleXml emptyExample = new ExampleXml()
    static String fullExampleXml = '<example expression="fullTestExpression" explanation="fullTestExplanation" remark="fullTestRemark"/>'
    static String mediumExampleXml = '<example expression="mediumTestExpression" explanation="mediumTestExplanation"/>'
    static String shortExampleXml = '<example expression="shortTestExpression"/>'
    static String emptyExampleXml = '<example/>'

    static NameXml fullName = new NameXml(value: 'fullTestValue', form: 'TestCategory1.TC1_1 TestCategory2.TC2_2')
    static NameXml shortName = new NameXml(value: 'shortTestValue')
    static String fullNameXml = '<name value="fullTestValue" form="TestCategory1.TC1_1 TestCategory2.TC2_2"/>'
    static String shortNameXml = '<name value="shortTestValue"/>'

    static FormExceptionXml fullFormException = new FormExceptionXml(pos: 'NOUN', names: [fullName, shortName])
    static FormExceptionXml shortFormException = new FormExceptionXml(pos: 'VERB', names: [shortName])
    static String fullFormExceptionXml = "<formException pos=\"NOUN\">$fullNameXml$shortNameXml</formException>"
    static String shortFormExceptionXml = "<formException pos=\"VERB\">$shortNameXml</formException>"

    static MeaningXml nullContentMeaning = new MeaningXml()
    static MeaningXml remarkOnlyMeaning = new MeaningXml(remark: 'onlyRemarkTest')
    static MeaningXml emptyContentMeaning = new MeaningXml(translations: [], examples: [])
    static MeaningXml translationsOnlyMeaning = new MeaningXml(translations: [fullTranslation, shortTranslation])
    static MeaningXml examplesOnlyMeaning = new MeaningXml(examples: [fullExample, shortExample])
    static MeaningXml mixedMeaning = new MeaningXml(translations: [shortTranslation], examples: [shortExample])
    static MeaningXml fullMeaning = new MeaningXml(remark: 'fullMeaningRemark', translations: [fullTranslation, shortTranslation], examples: [fullExample, shortExample])
    static String emptyMeaningXml = '<meaning/>'
    static String remarkOnlyMeaningXml = '<meaning remark="onlyRemarkTest"/>'
    static String translationsOnlyMeaningXml = "<meaning>${fullTranslationXml}${shortTranslationXml}</meaning>"
    static String examplesOnlyMeaningXml = "<meaning>${fullExampleXml}${shortExampleXml}</meaning>"
    static String mixedMeaningXml = "<meaning>${shortTranslationXml}${shortExampleXml}</meaning>"
    static String fullMeaningXml = "<meaning remark=\"fullMeaningRemark\">${fullTranslationXml}${shortTranslationXml}${fullExampleXml}${shortExampleXml}</meaning>"

    static PosBlockXml emptyPosBlock = new PosBlockXml(pos: "NOUN")
    static PosBlockXml shortPosBlock = new PosBlockXml(pos: "VERB", meanings: [translationsOnlyMeaning])
    static PosBlockXml fullPosBlock = new PosBlockXml(pos: "NOUN", meanings: [mixedMeaning, fullMeaning, examplesOnlyMeaning, translationsOnlyMeaning, nullContentMeaning])
    static String emptyPosBlockXml = '<posBlock pos="NOUN"/>'
    static String shortPosBlockXml = "<posBlock pos=\"VERB\">$translationsOnlyMeaningXml</posBlock>"
    static String fullPosBlockXml = "<posBlock pos=\"NOUN\">${mixedMeaningXml}${fullMeaningXml}${examplesOnlyMeaningXml}${translationsOnlyMeaningXml}${emptyMeaningXml}</posBlock>"

    static SemanticBlockXml emptySemanticBlock = new SemanticBlockXml()
    static SemanticBlockXml shortSemanticBlock = new SemanticBlockXml(posBlocks: [fullPosBlock])
    static SemanticBlockXml longSemanticBlock = new SemanticBlockXml(posBlocks: [shortPosBlock, emptyPosBlock])
    static String emptySemanticBlockXml = '<semanticBlock/>'
    static String shortSemanticBlockXml = "<semanticBlock>$fullPosBlockXml</semanticBlock>"
    static String longSemanticBlockXml = "<semanticBlock>${shortPosBlockXml}${emptyPosBlockXml}</semanticBlock>"

    static WordXml emptyWord = new WordXml(id: '_testId1', name: shortName)
    static WordXml fullWord = new WordXml(
            id: '_testId2',
            name: shortName,
            transcriptions: [shortTranscription, fullTranscription],
            semanticBlocks: [shortSemanticBlock, longSemanticBlock],
            formExceptions: [fullFormException])
    static String emptyWordXml = "<word id=\"_testId1\">$shortNameXml</word>"
    static String fullWordXml = "<word id=\"_testId2\">${shortNameXml}${shortTranscriptionXml}${fullTranscriptionXml}${shortSemanticBlockXml}${longSemanticBlockXml}${fullFormExceptionXml}</word>"

    static DictionaryXml emptyDictionary = new DictionaryXml(lang: 'Tt')
    static DictionaryXml fullDictionary = new DictionaryXml(lang: 'Tt', words: [fullWord, emptyWord])
    static String emptyDictionaryXml = '<dictionary lang="Tt"/>'
    static String fullDictionaryXml = "<dictionary lang=\"Tt\">${fullWordXml}${emptyWordXml}</dictionary>"
}
