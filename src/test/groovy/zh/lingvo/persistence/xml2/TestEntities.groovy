package zh.lingvo.persistence.xml2

import zh.lingvo.domain.Number
import zh.lingvo.domain.PartOfSpeech
import zh.lingvo.domain.forms.NounWordForm
import zh.lingvo.domain.words.Example
import zh.lingvo.domain.words.Meaning
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.PosBlock
import zh.lingvo.domain.words.SemanticBlock
import zh.lingvo.domain.words.Transcription
import zh.lingvo.domain.words.Translation
import zh.lingvo.domain.words.Word

class TestEntities {
    static Word getManWord() {
        def name = 'man'
        def transcription = new Transcription(ipa: 'man')

        def humanTranslation = new Translation(translation: 'человек')
        def humanMeaning = new Meaning(translations: [humanTranslation])

        def maleTranslation = new Translation(translation: 'мужчина')
        def maleMeaning = new Meaning(translations: [maleTranslation])

        def nounPosBlock = new PosBlock(PartOfSpeech.NOUN)
        nounPosBlock.meanings = [humanMeaning, maleMeaning]
        def semBlock = new SemanticBlock(posBlocks: [nounPosBlock])

        def pluralExceptionName = new Name(value: 'men', form: [Number.PLURAL, NounWordForm.NOMINATIVE])
        def formException = new HashMap<PartOfSpeech, List<Name>>()
        formException[PartOfSpeech.NOUN] = [pluralExceptionName]

        def manWord = new Word(UUID.fromString('7a631e0d-5d91-40c8-a0b2-b8cef2eb97e3'), name)
        manWord.transcriptions = [transcription]
        manWord.semanticBlocks = [semBlock]
        manWord.formExceptions = formException
        return manWord;
    }

    static Word getBoxWord() {
        def name = 'box'
        def transcription = new Transcription(ipa: 'bɒks')

        def boxTranslation1 = new Translation(translation: 'коробка')
        def boxTranslation2 = new Translation(translation: 'ящик')
        def boxTranslation3 = new Translation(translation: 'сундук')
        def boxTranslation4 = new Translation(translation: 'сумка, вместилище')
        def boxMeaning = new Meaning(
                translations: [boxTranslation1, boxTranslation2, boxTranslation3, boxTranslation4])

        def smallBoxTranslation1 = new Translation(translation: 'ящичек', elaboration: 'стола')
        def smallBoxTranslation2 = new Translation(translation: 'коробочка', elaboration: 'для всяких мелочей')
        def smallBoxTranslation3 = new Translation(translation: 'шкатулка')
        def smallBoxExample1 = new Example(expression: 'witness box', explanation: 'место в суде, где сидят свидетели')
        def smallBoxExample2 = new Example(expression: 'music box', explanation: 'музыкальная шкатулка', remark: 'амер.')
        def smallBoxExample3 = new Example(expression: 'musical box', explanation: 'музыкальная шкатулка', remark: 'брит.')
        def smallBoxExample4 = new Example(expression: 'shooting box', explanation: 'охотничья сумка')
        def smallBoxMeaning = new Meaning(
                translations: [smallBoxTranslation1, smallBoxTranslation2, smallBoxTranslation3],
                examples: [smallBoxExample1, smallBoxExample2, smallBoxExample3, smallBoxExample4])

        def religiousTranslation1 = new Translation(translation: 'дарохранительница')
        def religiousTranslation2 = new Translation(translation: 'дароносица')
        def religiousMeaning = new Meaning(
                remark: 'рел.',
                translations: [religiousTranslation1, religiousTranslation2])

        def nounBoxPosBlock = new PosBlock(PartOfSpeech.NOUN)
        nounBoxPosBlock.meanings = [boxMeaning, smallBoxMeaning, religiousMeaning]

        def containerSemBlock = new SemanticBlock(
                posBlocks: [nounBoxPosBlock])

        def hitNounTranslation = new Translation(translation: 'удар')
        def hitNounMeaning = new Meaning(translations: [hitNounTranslation])
        def hitNounPosBlock = new PosBlock(PartOfSpeech.NOUN)
        hitNounPosBlock.meanings = [hitNounMeaning]

        def hitVerbTranslation = new Translation(translation: 'боксировать')
        def hitVerbMeaning = new Meaning(translations: [hitVerbTranslation])
        def hitVerbPosBlock = new PosBlock(PartOfSpeech.VERB)
        hitVerbPosBlock.meanings = [hitVerbMeaning]

        def hitSemBlock = new SemanticBlock(posBlocks: [hitNounPosBlock, hitVerbPosBlock])

        def boxWord =  new Word(UUID.fromString('89b7820d-3e3d-4ea6-a7d5-190e485c5164'), name)
        boxWord.transcriptions = [transcription]
        boxWord.semanticBlocks = [containerSemBlock, hitSemBlock]
        return boxWord
    }
}
