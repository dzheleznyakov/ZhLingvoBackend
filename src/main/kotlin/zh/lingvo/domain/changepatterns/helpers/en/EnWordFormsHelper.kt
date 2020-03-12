package zh.lingvo.domain.changepatterns.helpers.en

import zh.lingvo.domain.changepatterns.ChangeModel
import zh.lingvo.domain.changepatterns.helpers.AbstractWordFormsHelper
import zh.lingvo.domain.linguisticcategories.LinguisticCategory
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word

private val SIBILANTS = setOf("s", "sh", "x")
private val VOWELS = setOf("e", "a", "o", "i", "u", "y")

abstract class EnWordFormsHelper : AbstractWordFormsHelper() {
    protected fun String.appendS() = when {
        endsWithSibilant() -> substitute(0, "es")
        endsWith("o") -> substitute(0, "es")
        endsWithDiphthongY() -> substitute(1, "ies")
        endsWithShortF() -> substitute(1, "ves")
        endsWithShortFE() -> substitute(2, "ves")
        else -> substitute(0, "s")
    }

    protected fun String.endsWithSibilant() = endingIsInSet(SIBILANTS)
    protected fun String.endsWithDiphthongY() = endsWith("y") && length > 1 && this[length - 2] != 'y' && !substring(0, length - 1).endingIsInSet(VOWELS)
    protected fun String.endsWithShortF() = endsWith("f") && substring(0, length - 1).endingIsInSet(VOWELS)
    protected fun String.endsWithShortFE() = endsWith("fe") && substring(0, length - 2).endingIsInSet(VOWELS)
}

abstract class EnLinguisticCategoryHelper : EnWordFormsHelper() {
    abstract fun getWordForms(word: String, formExceptions: List<Name> = listOf()): Pair<MutableMap<Array<LinguisticCategory>, String>, ChangeModel>

    override fun getForms(word: Word, formExceptions: List<Name>) = throw IllegalAccessException("This method is not supposed to be implemented")

    override fun getLanguageForms() = throw IllegalAccessException("This method is not supposed to be implemented")

    override fun getForm(formName: String) = throw IllegalAccessException("This method is not supposed to be implemented")
}
