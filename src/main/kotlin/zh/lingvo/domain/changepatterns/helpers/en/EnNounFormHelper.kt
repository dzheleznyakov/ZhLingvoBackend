package zh.lingvo.domain.changepatterns.helpers.en

import zh.lingvo.domain.changepatterns.BasicNounChangeModel
import zh.lingvo.domain.forms.NounDeclensionsCategory.PLURAL_NOMINATIVE
import zh.lingvo.domain.forms.NounDeclensionsCategory.PLURAL_POSSESSIVE
import zh.lingvo.domain.forms.NounDeclensionsCategory.SINGULAR_NOMINATIVE
import zh.lingvo.domain.forms.NounDeclensionsCategory.SINGULAR_POSSESSIVE
import zh.lingvo.domain.forms.WordForms
import zh.lingvo.domain.languages.English
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word
import java.util.*

private val FORMS = setOf(SINGULAR_NOMINATIVE, PLURAL_NOMINATIVE, SINGULAR_POSSESSIVE, PLURAL_POSSESSIVE)

private val CHANGE_MODEL = BasicNounChangeModel(English.getInstance())

class EnNounFormHelper : EnWordFormsHelper() {
    override fun getForms(word: Word, formExceptions: List<Name>): WordForms {
        val baseForm = word.name.value
        val pluralBaseForm = formExceptions.stream()
                .filter { Arrays.equals(it.form, PLURAL_NOMINATIVE) }
                .findAny()
                .map(Name::getValue)
                .orElseGet { baseForm.appendS() }
        val wordForms = mapOf(
                SINGULAR_NOMINATIVE to baseForm,
                PLURAL_NOMINATIVE to pluralBaseForm,
                SINGULAR_POSSESSIVE to "${baseForm}'s",
                PLURAL_POSSESSIVE to
                        if (pluralBaseForm.endsWith("s")) "${pluralBaseForm}'"
                        else "${pluralBaseForm}'s")
        return WordForms().apply { put(wordForms, CHANGE_MODEL) }
    }

    override fun getLanguageForms() = FORMS
}
