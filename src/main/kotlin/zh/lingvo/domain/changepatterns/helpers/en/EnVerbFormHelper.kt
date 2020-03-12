package zh.lingvo.domain.changepatterns.helpers.en

import com.google.common.reflect.ClassPath
import zh.lingvo.domain.changepatterns.BasicVerbChangeModel
import zh.lingvo.domain.changepatterns.ChangeModel
import zh.lingvo.domain.changepatterns.helpers.AbstractWordFormsHelper
import zh.lingvo.domain.changepatterns.helpers.VerbTimeTenseHelper
import zh.lingvo.domain.forms.WordForms
import zh.lingvo.domain.languages.English
import zh.lingvo.domain.linguisticcategories.LinguisticCategory
import zh.lingvo.domain.linguisticcategories.Number
import zh.lingvo.domain.linguisticcategories.Person
import zh.lingvo.domain.linguisticcategories.VerbTimeTense
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.Word

private val FORMS = {
    val timeTenses = VerbTimeTense.values()
    val numbers = Number.values()
    val persons = Person.values()
    timeTenses
            .flatMap { timeTense ->
                numbers.flatMap { number ->
                    persons.map { person -> Triple(timeTense, number, person) }
                }
            }
            .map { (first, second, third) -> arrayOf<LinguisticCategory>(first, second, third) }
            .toSet()
}.invoke()

private val CHANGE_MODEL = BasicVerbChangeModel(English.getInstance())

class EnVerbFormHelper : AbstractWordFormsHelper() {
    @Suppress("UnstableApiUsage")
    private val timeTenseHelpers: List<Pair<EnLinguisticCategoryHelper, String>> = ClassPath.from(javaClass.classLoader)
            .getTopLevelClassesRecursive("zh.lingvo.domain.changepatterns.helpers.en")
            .map(ClassPath.ClassInfo::load)
            .filter { EnLinguisticCategoryHelper::class.java.isAssignableFrom(it) }
            .filter { it.getDeclaredAnnotation(VerbTimeTenseHelper::class.java) != null }
            .map {
                @Suppress("UNCHECKED_CAST")
                val helper = it.getConstructor(ChangeModel::class.java).newInstance(CHANGE_MODEL) as EnLinguisticCategoryHelper
                val description = it.getDeclaredAnnotation(VerbTimeTenseHelper::class.java).description
                helper to description
            }
            .toList()

    override fun getForms(word: Word, formExceptions: List<Name>): WordForms {
        return WordForms().apply {
            timeTenseHelpers.map { (helper, description) ->
                val (forms, changeModel) = helper.getWordForms(word.name.value, formExceptions)
                put(forms.toMutableMap(), changeModel, description)
            }
        }
    }

    override fun getLanguageForms() = FORMS

}
