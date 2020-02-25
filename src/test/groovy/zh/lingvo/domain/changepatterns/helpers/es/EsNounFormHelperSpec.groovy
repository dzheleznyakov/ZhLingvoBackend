package zh.lingvo.domain.changepatterns.helpers.es

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.domain.LinguisticCategory
import zh.lingvo.domain.NounLinguisticCategories
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper
import zh.lingvo.domain.words.Word

class EsNounFormHelperSpec extends Specification {
    private WordFormsHelper helper = new EsNounFormHelper()

    private static LinguisticCategory[] snKey = NounLinguisticCategories.SINGULAR_NOMINATIVE
    private static LinguisticCategory[] pnKey = NounLinguisticCategories.PLURAL_NOMINATIVE

    @Unroll
    def "Test forms for word [#wordName]"() {
        given: 'a word'
        Word word = [UUID.randomUUID(), wordName]

        when: 'the wordName worms are requested'
        def actualForms = helper.getForms(word, [])

        then: 'the returned forms are correct'
        actualForms == expectedForms

        where: 'the parameters are'
        wordName  || expectedForms
        'casa'    || toChangeModel('casa', 'casas')
        'hotel'   || toChangeModel('hotel', 'hoteles')
        'edad'    || toChangeModel('edad', 'edades')
        'pensión' || toChangeModel('pensión', 'pensiones')
        'acción'  || toChangeModel('acción', 'acciones')
        'lápiz'   || toChangeModel('lápiz', 'lápices')
//        'joven'   || toChangeModel('joven', 'jóvenes')
    }

    private static def toChangeModel(sn, pn) {
        [(snKey):sn, (pnKey):pn]
    }
}
