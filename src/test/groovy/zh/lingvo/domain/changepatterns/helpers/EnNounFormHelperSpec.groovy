package zh.lingvo.domain.changepatterns.helpers

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.domain.changepatterns.ChangeModel
import zh.lingvo.domain.languages.English
import zh.lingvo.domain.languages.Language
import zh.lingvo.domain.words.Word

import static zh.lingvo.domain.PartOfSpeech.NOUN

class EnNounFormHelperSpec extends Specification {
    private Language english = English.instance
    private WordFormsHelper helper = new EnNounFormHelper()
    private ChangeModel changeModel = english.getChangeModel(NOUN)

    @Unroll
    def "Test change forms for [#wordName]"() {
        given: 'a word'
        Word word = [UUID.randomUUID(), wordName]

        when: 'the wordName worms are requested'
        def actualForms = helper.getForms(word)

        then: 'the returned forms are correct'
        actualForms == expectedForms

        where: 'the parameters are'
        wordName || expectedForms
        'box'    || toChangeModel('box', 'boxes', "box's", "boxes'")
        'book'   || toChangeModel('book', 'books', "book's", "books'")
        'stash'  || toChangeModel('stash', 'stashes', "stash's", "stashes'")
        'pass'   || toChangeModel('pass', 'passes', "pass's", "passes'")
        'boy'    || toChangeModel('boy', 'boys', "boy's", "boys'")
        'fly'    || toChangeModel('fly', 'flies', "fly's", "flies'")
        'cliff'  || toChangeModel('cliff', 'cliffs', "cliff's", "cliffs'")
        'leaf'   || toChangeModel('leaf', 'leaves', "leaf's", "leaves'")
        'wife'   || toChangeModel('wife', 'wives', "wife's", "wives'")
    }

    private static Enum[] snKey = EnNounFormHelper.SINGULAR_NOMINATIVE
    private static Enum[] pnKey = EnNounFormHelper.PLURAL_NOMINATIVE
    private static Enum[] spKey = EnNounFormHelper.SINGULAR_POSSESSIVE
    private static Enum[] ppKey = EnNounFormHelper.PLURAL_POSSESSIVE
    private static def toChangeModel(sn, pn, sp, pp) {
        [(snKey):sn, (pnKey):pn, (spKey):sp, (ppKey):pp]
    }
}
