package zh.lingvo.domain.changepatterns.helpers

import com.google.common.base.Preconditions
import zh.lingvo.domain.LinguisticCategory
import zh.lingvo.domain.forms.WordForms
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word

interface  WordFormsHelper {
    fun getForms(word: Word, formExceptions: List<Name>?): WordForms
    fun getLanguageForms(): Set<Array<LinguisticCategory>>
    fun getForm(formName: String): Array<LinguisticCategory>
}

abstract class AbstractWordFormsHelper : WordFormsHelper {
    override fun getForm(formName: String): Array<LinguisticCategory> {
        val parts = formName.split(".")
        if (parts.size != 2)
            throw IllegalArgumentException("Form name for a noun should have 2 parts, found ${parts.size}, [${formName}]")
        else
            return getLanguageForms().stream()
                    .filter { form -> form[0].name() == parts[0] && form[1].name() == parts[1]}
                    .findAny()
                    .orElseThrow { IllegalArgumentException("Illegal form name for this noun: $formName") }
    }

    protected fun String.substitute(backOffset: Int, suffix: String): String {
        Preconditions.checkArgument(backOffset >= 0, "backOffset [%d] should be greater or equal to 0", backOffset)
        Preconditions.checkArgument(backOffset <= length, "backOffset [%d] should be less or equal than the length of [%s]", backOffset, this)
        return substring(0, length - backOffset) + suffix
    }

    protected fun String.endingIsInSet(set: Set<String>) = set.stream().anyMatch { el -> this.endsWith(el) }
}
