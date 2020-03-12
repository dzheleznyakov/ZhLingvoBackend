package zh.lingvo.domain.changepatterns.helpers.es


import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper
import zh.lingvo.domain.forms.NounDeclensionsCategory
import zh.lingvo.domain.linguisticcategories.LinguisticCategory
import zh.lingvo.domain.words.Word

import java.util.stream.Collectors

class EsNounFormHelperSpec extends Specification {
    private WordFormsHelper helper = new EsNounFormHelper()

    private static LinguisticCategory[] snKey = NounDeclensionsCategory.SINGULAR_NOMINATIVE
    private static LinguisticCategory[] pnKey = NounDeclensionsCategory.PLURAL_NOMINATIVE

    @Unroll
    def "Test forms for word [#wordName]"() {
        given: 'a word'
        Word word = [UUID.randomUUID(), wordName]

        when: 'the wordName worms are requested'
        def actualForms = helper.getForms(word, []).allWordForms
                .stream()
                .map { it.wordForms }
                .collect(Collectors.toList())

        then: 'the returned forms are correct'
        actualForms.size() == 1
        actualForms[0] == expectedForms

        where: 'the parameters are'
        wordName  || expectedForms
        'casa'    || toForms('casa', 'casas')
        'hotel'   || toForms('hotel', 'hoteles')
        'edad'    || toForms('edad', 'edades')
        'pensión' || toForms('pensión', 'pensiones')
        'acción'  || toForms('acción', 'acciones')
        'lápiz'   || toForms('lápiz', 'lápices')
        'joven'   || toForms('joven', 'jóvenes')
    }

    private static def toForms(sn, pn) {
        [(snKey):sn, (pnKey):pn]
    }
}
