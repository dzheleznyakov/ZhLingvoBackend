package zh.lingvo.persistence.xml

import org.springframework.stereotype.Service
import zh.lingvo.caches.LanguagesCache
import zh.lingvo.domain.Dictionary
import zh.lingvo.domain.LinguisticCategory
import zh.lingvo.domain.Number
import zh.lingvo.domain.PartOfSpeech
import zh.lingvo.domain.forms.NounWordFormCategory
import zh.lingvo.domain.words.Example
import zh.lingvo.domain.words.Meaning
import zh.lingvo.domain.words.Name
import zh.lingvo.domain.words.PosBlock
import zh.lingvo.domain.words.SemanticBlock
import zh.lingvo.domain.words.Transcription
import zh.lingvo.domain.words.Translation
import zh.lingvo.domain.words.Word
import zh.lingvo.persistence.PersistenceException
import zh.lingvo.persistence.xml.entities.DictionaryXml
import zh.lingvo.persistence.xml.entities.ExampleXml
import zh.lingvo.persistence.xml.entities.FormExceptionXml
import zh.lingvo.persistence.xml.entities.MeaningXml
import zh.lingvo.persistence.xml.entities.NameXml
import zh.lingvo.persistence.xml.entities.PosBlockXml
import zh.lingvo.persistence.xml.entities.SemanticBlockXml
import zh.lingvo.persistence.xml.entities.TranscriptionXml
import zh.lingvo.persistence.xml.entities.TranslationXml
import zh.lingvo.persistence.xml.entities.WordXml
import zh.lingvo.util.CollectionUtils
import java.util.*

@Service
class DictionaryFactoryKotlin(val languagesCache: LanguagesCache) {
    fun getDictionary(dictionaryXml: DictionaryXml) = dictionaryXml.toDictionary()

    private fun DictionaryXml.toDictionary(): Dictionary = this.lang
            .run { languagesCache.get(this) }
            .run { Dictionary(this) }
            .apply {
                val source = this@toDictionary
                CollectionUtils.getNotNull { source.words }
                        .map { it.toWord() }
                        .forEach(this::add)
            }

    private fun WordXml.toWord(): Word {
                val id = if (id.startsWith("uuid")) UUID.fromString(this.id.substring("uuid".length))
                         else throw PersistenceException("Illegal word id format: [${this.id}]")
                val source = this
                return Word(id, source.name.value).apply {
                    transcriptions = source.toTranscriptions()
                    semanticBlocks = source.toSemanticBlocks()
                    formExceptions = source.toFormExceptions()
                }
            }

    private fun WordXml.toTranscriptions(): MutableList<Transcription> = CollectionUtils.transform(this::getTranscriptions) { it.toTranscription() }

    private fun TranscriptionXml.toTranscription() = Transcription(remark, ipa)

    private fun WordXml.toSemanticBlocks(): MutableList<SemanticBlock> = CollectionUtils.transform(this::getSemanticBlocks) { it.toSemanticBlock() }

    private fun SemanticBlockXml.toSemanticBlock() = SemanticBlock().apply {
        posBlocks = CollectionUtils.transform(this@toSemanticBlock::getPosBlocks) { it.toPosBlock() }
    }

    private fun PosBlockXml.toPosBlock() = PosBlock(PartOfSpeech.valueOf(pos)).apply {
        meanings = CollectionUtils.transform(this@toPosBlock::getMeanings) { it.toMeaning() }
    }

    private fun MeaningXml.toMeaning() = Meaning().apply {
        val source = this@toMeaning
        remark = source.remark
        translations = CollectionUtils.transform(source::getTranslations) { it.toTranslation()}
        examples = CollectionUtils.transform(source::getExamples) { it.toExample() }
    }

    private fun TranslationXml.toTranslation() = Translation(this.value).apply {
        elaboration = this@toTranslation.elaboration
    }

    private fun ExampleXml.toExample() = Example().apply {
        val source = this@toExample
        expression = source.expression
        explanation = source.explanation
        remark = source.remark
    }

    private fun WordXml.toFormExceptions(): MutableMap<PartOfSpeech, MutableList<Name>> = CollectionUtils.transformToMap(
            this::getFormExceptions, { PartOfSpeech.valueOf(it.pos) }, { it.toName() })

    private fun FormExceptionXml.toName() = CollectionUtils.transform(this::getNames) { nameXml -> nameXml.toName() }

    private fun NameXml.toName(): Name = form.split(" ")
            .map { formPart -> formPart.split(".") }
            .map { getLinguisticCategory(it[0], it[1]) }
            .toTypedArray()
            .run {
                Name().apply {
                    value = this@toName.value
                    form = this@run
                }
            }

    private fun getLinguisticCategory(clazz: String, name: String): LinguisticCategory = when(clazz) {
        Number::class.java.simpleName -> Number.valueOf(name)
        NounWordFormCategory::class.java.simpleName -> NounWordFormCategory.valueOf(name)
        else -> throw IllegalArgumentException("Unknown category class [${clazz}]")
    }
}
