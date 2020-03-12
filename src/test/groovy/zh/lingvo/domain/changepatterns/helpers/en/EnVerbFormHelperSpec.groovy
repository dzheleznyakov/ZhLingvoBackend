package zh.lingvo.domain.changepatterns.helpers.en

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper
import zh.lingvo.domain.forms.VerbConjugationCategory
import zh.lingvo.domain.linguisticcategories.LinguisticCategory
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word

class EnVerbFormHelperSpec extends Specification {
    private WordFormsHelper helper = new EnVerbFormHelper()

    private static LinguisticCategory[] sfKey = VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_FIRST
    private static LinguisticCategory[] pfKey = VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_FIRST
    private static LinguisticCategory[] ssKey = VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_SECOND
    private static LinguisticCategory[] psKey = VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_SECOND
    private static LinguisticCategory[] stKey = VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_THIRD
    private static LinguisticCategory[] ptKey = VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_THIRD

    @Unroll
    def "Test Present Simple forms for [#wordName]"() {
        given: 'a word'
        Word word = [UUID.randomUUID(), wordName]

        when: 'the wordName forms are requested'
        def actualWordForms = helper.getForms(word, formException).allWordForms
                .stream()
                .filter { it.description == 'Present Simple'}
                .map { it.wordForms }
                .findAny()
                .orElse(null)

        then: 'the returned forms are correct'
        actualWordForms != null
        actualWordForms == expectedForms

        where: 'the parameters are'
        wordName | formException                         || expectedForms
        'get'    | []                                    || toPresentSimpleForms('get', 'gets')
        'box'    | []                                    || toPresentSimpleForms('box', 'boxes')
        'hush'   | []                                    || toPresentSimpleForms('hush', 'hushes')
        'do'     | []                                    || toPresentSimpleForms('do', 'does')
        'have'   | [new Name(value: 'has', form: stKey)] || toPresentSimpleForms('have', 'has')
    }

    private def toPresentSimpleForms(base, third) {
        [(sfKey): base, (pfKey): base, (ssKey): base, (psKey): base, (stKey): third, (ptKey): base]
    }

}
