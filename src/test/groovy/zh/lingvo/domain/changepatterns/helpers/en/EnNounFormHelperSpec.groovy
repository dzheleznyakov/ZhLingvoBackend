package zh.lingvo.domain.changepatterns.helpers.en

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.domain.forms.NounDeclensionsCategory
import zh.lingvo.domain.linguisticcategories.LinguisticCategory
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word

import java.util.stream.Collectors

class EnNounFormHelperSpec extends Specification {
    private WordFormsHelper helper = new EnNounFormHelper()

    private static LinguisticCategory[] snKey = NounDeclensionsCategory.SINGULAR_NOMINATIVE
    private static LinguisticCategory[] pnKey = NounDeclensionsCategory.PLURAL_NOMINATIVE
    private static LinguisticCategory[] spKey = NounDeclensionsCategory.SINGULAR_POSSESSIVE
    private static LinguisticCategory[] ppKey = NounDeclensionsCategory.PLURAL_POSSESSIVE

    @Unroll
    def "Test change forms for [#wordName]"() {
        given: 'a word'
        Word word = [UUID.randomUUID(), wordName]

        when: 'the wordName forms are requested'
        def actualForms = helper.getForms(word, formExceptions).allWordForms
                .stream()
                .map { it.wordForms }
                .collect(Collectors.toList())

        then: 'the returned forms are correct'
        actualForms.size() == 1
        actualForms[0] == expectedForms

        where: 'the parameters are'
        wordName | formExceptions                        || expectedForms
        'box'    | []                                    || toForms('box', 'boxes', "box's", "boxes'")
        'book'   | []                                    || toForms('book', 'books', "book's", "books'")
        'stash'  | []                                    || toForms('stash', 'stashes', "stash's", "stashes'")
        'pass'   | []                                    || toForms('pass', 'passes', "pass's", "passes'")
        'boy'    | []                                    || toForms('boy', 'boys', "boy's", "boys'")
        'fly'    | []                                    || toForms('fly', 'flies', "fly's", "flies'")
        'cliff'  | []                                    || toForms('cliff', 'cliffs', "cliff's", "cliffs'")
        'leaf'   | []                                    || toForms('leaf', 'leaves', "leaf's", "leaves'")
        'wife'   | []                                    || toForms('wife', 'wives', "wife's", "wives'")
        'man'    | [new Name(value: 'men', form: pnKey)] || toForms('man', 'men', "man's", "men's")
    }
    private static def toForms(sn, pn, sp, pp) {
        [(snKey):sn, (pnKey):pn, (spKey):sp, (ppKey):pp]
    }
}
