package zh.lingvo.domain.changepatterns.helpers.en

import zh.lingvo.domain.changepatterns.ChangeModel
import zh.lingvo.domain.changepatterns.helpers.VerbTimeTenseHelper
import zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_FIRST
import zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_SECOND
import zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_THIRD
import zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_FIRST
import zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_SECOND
import zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_THIRD
import zh.lingvo.domain.linguisticcategories.LinguisticCategory
import zh.lingvo.domain.linguisticcategories.VerbTimeTense
import zh.lingvo.domain.words.Name
import java.util.*

@VerbTimeTenseHelper(value = VerbTimeTense.PRESENT_SIMPLE, description = "Present Simple")
class EnVerbSimplePresentHelper(changeModel: ChangeModel) : EnLinguisticCategoryHelper() {

    private val changeModel = changeModel

    override fun getWordForms(word: String, formExceptions: List<Name>): Pair<MutableMap<Array<LinguisticCategory>, String>, ChangeModel> {
        val thirdSingular = formExceptions.stream()
                .filter { Arrays.equals(it.form, PRESENT_SIMPLE_SINGULAR_THIRD) }
                .findAny()
                .map(Name::getValue)
                .orElseGet { word.appendS() }
        val forms = mutableMapOf(
                PRESENT_SIMPLE_SINGULAR_FIRST to word,
                PRESENT_SIMPLE_PLURAL_FIRST to word,
                PRESENT_SIMPLE_SINGULAR_SECOND to word,
                PRESENT_SIMPLE_PLURAL_SECOND to word,
                PRESENT_SIMPLE_SINGULAR_THIRD to thirdSingular,
                PRESENT_SIMPLE_PLURAL_THIRD to word)
        return forms to this.changeModel;
    }
}
