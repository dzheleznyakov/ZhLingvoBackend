package zh.lingvo.domain.changepatterns.helpers.en

import zh.lingvo.domain.LinguisticCategory
import zh.lingvo.domain.NounLinguisticCategories.PLURAL_NOMINATIVE
import zh.lingvo.domain.NounLinguisticCategories.PLURAL_POSSESSIVE
import zh.lingvo.domain.NounLinguisticCategories.SINGULAR_NOMINATIVE
import zh.lingvo.domain.NounLinguisticCategories.SINGULAR_POSSESSIVE
import zh.lingvo.domain.changepatterns.helpers.AbstractWordFormsHelper
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word
import java.util.*

private val FORMS = setOf(SINGULAR_NOMINATIVE, PLURAL_NOMINATIVE, SINGULAR_POSSESSIVE, PLURAL_POSSESSIVE)
private val SIBILANTS = setOf("s", "sh", "x")
private val VOWELS = setOf("e", "a", "o", "i", "u", "y")

class EnNounFormHelper : AbstractWordFormsHelper() {
    override fun getForms(word: Word, formExceptions: List<Name>?): Map<Array<LinguisticCategory>, String> {
        val baseForm = word.name.value
        val pluralBaseForm = (formExceptions ?: listOf())
                .stream()
                .filter { Arrays.equals(it.form, PLURAL_NOMINATIVE) }
                .findAny()
                .map(Name::getValue)
                .orElseGet { baseForm.appendS() }
        return mapOf(
                SINGULAR_NOMINATIVE to baseForm,
                PLURAL_NOMINATIVE to pluralBaseForm,
                SINGULAR_POSSESSIVE to "${baseForm}'s",
                PLURAL_POSSESSIVE to
                        if (pluralBaseForm.endsWith("s")) "${pluralBaseForm}'"
                        else "${pluralBaseForm}'s")
    }

    private fun String.appendS() = when {
        endsWithSibilant() -> substitute(0, "es")
        endsWithDiphthongY() -> substitute(1, "ies")
        endsWithShortF() -> substitute(1, "ves")
        endsWithShortFE() -> substitute(2, "ves")
        else -> substitute(0, "s")
    }

    private fun String.endsWithSibilant() = endingIsInSet(SIBILANTS)
    private fun String.endsWithDiphthongY() = endsWith("y") && length > 1 && this[length - 2] != 'y' && !substring(0, length - 1).endingIsInSet(VOWELS)
    private fun String.endsWithShortF() = endsWith("f") && substring(0, length - 1).endingIsInSet(VOWELS)
    private fun String.endsWithShortFE() = endsWith("fe") && substring(0, length - 2).endingIsInSet(VOWELS)

    override fun getLanguageForms() = FORMS
}
