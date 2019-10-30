package zh.lingvo.persistence.xml2


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

interface Constants {
    static TranscriptionXml fullTranscription = ['fullTestRemark', 'fullTestIpa']
    static TranscriptionXml shortTranscription = [null, 'shortTestIpa']
    static String fullTranscriptionXml = '<transcription remark="fullTestRemark" ipa="fullTestIpa"/>'
    static String shortTranscriptionXml = '<transcription ipa="shortTestIpa"/>'

    static TranslationXml fullTranslation = ['fullTestValue', 'fullTestElaboration']
    static TranslationXml shortTranslation = ['shortTestValue', null]
    static String fullTranslationXml = '<translation value="fullTestValue" elaboration="fullTestElaboration"/>'
    static String shortTranslationXml = '<translation value="shortTestValue"/>'

    static ExampleXml fullExample = ['fullTestExpression', 'fullTestExplanation', 'fullTestRemark']
    static ExampleXml mediumExample = ['mediumTestExpression', 'mediumTestExplanation', null]
    static ExampleXml shortExample = ['shortTestExpression', null, null]
    static ExampleXml emptyExample = []
    static String fullExampleXml = '<example expression="fullTestExpression" explanation="fullTestExplanation" remark="fullTestRemark"/>'
    static String mediumExampleXml = '<example expression="mediumTestExpression" explanation="mediumTestExplanation"/>'
    static String shortExampleXml = '<example expression="shortTestExpression"/>'
    static String emptyExampleXml = '<example/>'

    static NameXml fullName = ['fullTestValue', 'TestCategory1.TC1_1 TestCategory2.TC2_2']
    static NameXml shortName = ['shortTestValue', null]
    static String fullNameXml = '<name value="fullTestValue" form="TestCategory1.TC1_1 TestCategory2.TC2_2"/>'
    static String shortNameXml = '<name value="shortTestValue"/>'

    static FormExceptionXml fullFormException = ['NOUN', [fullName, shortName]]
    static FormExceptionXml shortFormException = ['VERB', [shortName]]
    static String fullFormExceptionXml = "<formException pos=\"NOUN\">$fullNameXml$shortNameXml</formException>"
    static String shortFormExceptionXml = "<formException pos=\"VERB\">$shortNameXml</formException>"

    static MeaningXml nullContentMeaning = [null, null]
    static MeaningXml emptyContentMeaning = [[], []]
    static MeaningXml translationsOnlyMeaning = [[fullTranslation, shortTranslation], null]
    static MeaningXml examplesOnlyMeaning = [null, [fullExample, shortExample]]
    static MeaningXml fullMeaning = [[fullTranslation, shortTranslation], [fullExample, shortExample]]
    static MeaningXml mixedMeaning = [[shortTranslation], [shortExample]]
    static String emptyMeaningXml = '<meaning/>'
    static String translationsOnlyMeaningXml = "<meaning>${fullTranslationXml}${shortTranslationXml}</meaning>"
    static String examplesOnlyMeaningXml = "<meaning>${fullExampleXml}${shortExampleXml}</meaning>"
    static String mixedMeaningXml = "<meaning>${shortTranslationXml}${shortExampleXml}</meaning>"
    static String fullMeaningXml = "<meaning>${fullTranslationXml}${shortTranslationXml}${fullExampleXml}${shortExampleXml}</meaning>"

    static PosBlockXml emptyPosBlock = ["NOUN", null]
    static PosBlockXml shortPosBlock = ["VERB", [translationsOnlyMeaning]]
    static PosBlockXml fullPosBlock = ["NOUN", [mixedMeaning, fullMeaning, examplesOnlyMeaning, translationsOnlyMeaning, nullContentMeaning]]
    static String emptyPosBlockXml = '<posBlock pos="NOUN"/>'
    static String shortPosBlockXml = "<posBlock pos=\"VERB\">$translationsOnlyMeaningXml</posBlock>"
    static String fullPosBlockXml = "<posBlock pos=\"NOUN\">${mixedMeaningXml}${fullMeaningXml}${examplesOnlyMeaningXml}${translationsOnlyMeaningXml}${emptyMeaningXml}</posBlock>"

    static SemanticBlockXml emptySemanticBlock = []
    static SemanticBlockXml shortSemanticBlock = [[fullPosBlock]]
    static SemanticBlockXml longSemanticBlock=[[shortPosBlock, emptyPosBlock]]
    static String emptySemanticBlockXml = '<semanticBlock/>'
    static String shortSemanticBlockXml = "<semanticBlock>$fullPosBlockXml</semanticBlock>"
    static String longSemanticBlockXml = "<semanticBlock>${shortPosBlockXml}${emptyPosBlockXml}</semanticBlock>"

    static WordXml emptyWord = ['_testId1', shortName, null, null, null]
    static WordXml fullWord = ['_testId2', shortName, [shortTranscription, fullTranscription], [shortSemanticBlock, longSemanticBlock], [fullFormException]]
    static String emptyWordXml = "<word id=\"_testId1\">$shortNameXml</word>"
    static String fullWordXml = "<word id=\"_testId2\">${shortNameXml}${shortTranscriptionXml}${fullTranscriptionXml}${shortSemanticBlockXml}${longSemanticBlockXml}${fullFormExceptionXml}</word>"

    static DictionaryXml emptyDictionary = ['Tt', null]
    static DictionaryXml fullDictionary = ['Tt', [fullWord, emptyWord]]
    static String emptyDictionaryXml = '<dictionary lang="Tt"/>'
    static String fullDictionaryXml = "<dictionary lang=\"Tt\">${fullWordXml}${emptyWordXml}</dictionary>"


}
