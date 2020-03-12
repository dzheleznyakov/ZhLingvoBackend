package zh.lingvo.domain.changepatterns.helpers.es

import zh.lingvo.domain.changepatterns.BasicNounChangeModel
import zh.lingvo.domain.changepatterns.helpers.AbstractWordFormsHelper
import zh.lingvo.domain.forms.NounDeclensionsCategory.PLURAL_NOMINATIVE
import zh.lingvo.domain.forms.NounDeclensionsCategory.SINGULAR_NOMINATIVE
import zh.lingvo.domain.forms.WordForms
import zh.lingvo.domain.languages.Spanish
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word

private val FORMS = setOf(SINGULAR_NOMINATIVE, PLURAL_NOMINATIVE)
private val BASE_VOWELS = setOf("e", "a", "o", "i", "u", "y")
private val VOWELS = setOf("e", "a", "o", "i", "u", "y", "é", "á", "ó", "í", "ú", "ü")
private val WEAK_CONSONANTS = setOf("s", "n")

private val STRESSED_VOWEL_PATTERN = ".*[éáóíú].*".toRegex().toPattern()

private val CHANGE_MODEL = BasicNounChangeModel(Spanish.getInstance())

class EsNounFormHelper : AbstractWordFormsHelper() {
    override fun getForms(word: Word, formExceptions: List<Name>): WordForms {
        val baseForm = word.name.value
        val wordForms = mapOf(
                SINGULAR_NOMINATIVE to baseForm,
                PLURAL_NOMINATIVE to baseForm.appendS())
        return WordForms().apply { put(wordForms, CHANGE_MODEL) }
    }

    private fun String.appendS() = when {
        endsWithVowel() -> substitute(0, "s")
        endsWithIon() -> substitute(2, "ones")
        endsWith("z") -> substitute(1, "ces")
        endsWithWeakConsonant() && !hasSetStress() -> setStress().substitute(0, "es")
        else -> substitute(0, "es")
    }

    private fun String.endsWithVowel() = endingIsInSet(VOWELS)
    private fun String.endsWithIon() = endsWith("ción") || endsWith("sión")
    private fun String.endsWithWeakConsonant() = endingIsInSet(WEAK_CONSONANTS)
    private fun String.hasSetStress() = STRESSED_VOWEL_PATTERN.matcher(this).matches()

    private fun String.setStress(): String {
        var count = 0
        for (i in this.indices.reversed()) {
            if (BASE_VOWELS.contains(this[i].toString())) count++
            if (count == 2) return substring(0, i) + this[i].setStress() + substring(i + 1)
        }
        return this
    }

    private fun Char.setStress() = when(this) {
        'e' -> 'é'
        'a' -> 'á'
        'o' -> 'ó'
        'i' -> 'í'
        'u' -> 'ú'
        else -> this
    }

    override fun getLanguageForms() = FORMS;
}
