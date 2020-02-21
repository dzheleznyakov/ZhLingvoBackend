package zh.lingvo.domain.changepatterns.helpers

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.domain.LinguisticCategory
import zh.lingvo.domain.changepatterns.ChangeModel
import zh.lingvo.domain.languages.English
import zh.lingvo.domain.languages.Language
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word

import static zh.lingvo.domain.PartOfSpeech.NOUN

class EnNounFormHelperSpec extends Specification {
    private Language english = English.instance
    private WordFormsHelper helper = new EnNounFormHelper()
    private ChangeModel changeModel = english.getChangeModel(NOUN)

    private static LinguisticCategory[] snKey = EnNounFormHelper.SINGULAR_NOMINATIVE
    private static LinguisticCategory[] pnKey = EnNounFormHelper.PLURAL_NOMINATIVE
    private static LinguisticCategory[] spKey = EnNounFormHelper.SINGULAR_POSSESSIVE
    private static LinguisticCategory[] ppKey = EnNounFormHelper.PLURAL_POSSESSIVE

    @Unroll
    def "Test change forms for [#wordName]"() {
        given: 'a word'
        Word word = [UUID.randomUUID(), wordName]

        when: 'the wordName worms are requested'
        def actualForms = helper.getForms(word, formExceptions)

        then: 'the returned forms are correct'
        actualForms == expectedForms

        where: 'the parameters are'
        wordName | formExceptions                        || expectedForms
        'box'    | []                                    || toChangeModel('box', 'boxes', "box's", "boxes'")
        'book'   | []                                    || toChangeModel('book', 'books', "book's", "books'")
        'stash'  | []                                    || toChangeModel('stash', 'stashes', "stash's", "stashes'")
        'pass'   | []                                    || toChangeModel('pass', 'passes', "pass's", "passes'")
        'boy'    | []                                    || toChangeModel('boy', 'boys', "boy's", "boys'")
        'fly'    | []                                    || toChangeModel('fly', 'flies', "fly's", "flies'")
        'cliff'  | []                                    || toChangeModel('cliff', 'cliffs', "cliff's", "cliffs'")
        'leaf'   | []                                    || toChangeModel('leaf', 'leaves', "leaf's", "leaves'")
        'wife'   | []                                    || toChangeModel('wife', 'wives', "wife's", "wives'")
        'man'    | [new Name(value: 'men', form: pnKey)] || toChangeModel('man', 'men', "man's", "men's")
    }
    private static def toChangeModel(sn, pn, sp, pp) {
        [(snKey):sn, (pnKey):pn, (spKey):sp, (ppKey):pp]
    }
}
