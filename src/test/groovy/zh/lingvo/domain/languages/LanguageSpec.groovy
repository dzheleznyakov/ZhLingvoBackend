package zh.lingvo.domain.languages

import spock.lang.Specification
import spock.lang.Unroll

import static zh.lingvo.domain.Declension.FIRST_PLURAL
import static zh.lingvo.domain.Declension.FIRST_SINGULAR
import static zh.lingvo.domain.Declension.SECOND
import static zh.lingvo.domain.Declension.SECOND_PLURAL
import static zh.lingvo.domain.Declension.SECOND_SINGULAR
import static zh.lingvo.domain.Declension.THIRD_PLURAL
import static zh.lingvo.domain.Declension.THIRD_SINGULAR
import static zh.lingvo.domain.Gender.FEMININE
import static zh.lingvo.domain.Gender.MASCULINE
import static zh.lingvo.domain.Gender.NEUTRAL
import static zh.lingvo.domain.Number.PLURAL
import static zh.lingvo.domain.Number.SINGULAR
import static zh.lingvo.domain.PartOfSpeech.ADJECTIVE
import static zh.lingvo.domain.PartOfSpeech.NOUN
import static zh.lingvo.domain.PartOfSpeech.VERB
import static zh.lingvo.domain.forms.NounWordForm.ACCUSATIVE
import static zh.lingvo.domain.forms.NounWordForm.DATIVE
import static zh.lingvo.domain.forms.NounWordForm.GENITIVE
import static zh.lingvo.domain.forms.NounWordForm.INSTRUMENTAL
import static zh.lingvo.domain.forms.NounWordForm.NOMINATIVE
import static zh.lingvo.domain.forms.NounWordForm.POSSESSIVE
import static zh.lingvo.domain.forms.NounWordForm.PREPOSITIONAL

class LanguageSpec extends Specification {
    private static final Language english = English.getInstance()
    private static final Language spanish = Spanish.getInstance()
    private static final Language russian = Russian.getInstance()

    @Unroll
    def "Test parts of speeches in In #language.getClass().simpleName"() {
        expect:
        language.partsOfSpeech == expectedPos

        where: 'the parameters are'
        language || expectedPos
        english  || [NOUN, VERB, ADJECTIVE]
        spanish  || [NOUN, VERB, ADJECTIVE]
        russian  || [NOUN, VERB, ADJECTIVE]
    }

    @Unroll
    def "In #language.getClass().simpleName, #pos is '#posNameInLanguage'"() {
        when: 'the part of speech\'es name is requested'
        def posName = language.getPartsOfSpeechName(pos)

        then: 'it should be translation of the part of speech to the language'
        posName == posNameInLanguage

        where: 'the parameters are'
        language | pos       || posNameInLanguage
        english  | NOUN      || 'noun'
        english  | VERB      || 'verb'
        english  | ADJECTIVE || 'adj'
        //
        spanish  | NOUN      || 'nombre'
        spanish  | VERB      || 'verbo'
        spanish  | ADJECTIVE || 'adj'
        //
        russian  | NOUN      || 'сущ'
        russian  | VERB      || 'гл'
        russian  | ADJECTIVE || 'прил'
    }

    @Unroll
    def "Test (linguistic) numbers in #language.getClass().simpleName"() {
        expect: 'All numbers in the language to be returned in order'
        language.numbers == expectedNumbers

        where: 'the parameters are'
        language || expectedNumbers
        english  || [SINGULAR, PLURAL]
        spanish  || [SINGULAR, PLURAL]
        russian  || [SINGULAR, PLURAL]
    }

    @Unroll
    def "In #language.getClass().simpleName, #num is '#numNameInLanguage'"() {
        when: 'the number name is requested'
        def numName = language.getNumberName(num)

        then: 'then the short form of the number in the target language is returned'
        numName == numNameInLanguage

        where: 'the parameters are'
        language | num      || numNameInLanguage
        english  | SINGULAR || 'sing'
        english  | PLURAL   || 'pl'
        //
        spanish  | SINGULAR || 'sing'
        spanish  | PLURAL   || 'pl'
        //
        russian  | SINGULAR || 'ед'
        russian  | PLURAL   || 'мн'

    }

    @Unroll
    def "Test declensions in #language.getClass().simpleName"() {
        expect: 'all declensions in the language to be returned in order'
        language.declensions == expectedDeclensions

        where: 'the parameters are'
        language || expectedDeclensions
        english  || [FIRST_SINGULAR, FIRST_PLURAL, SECOND, THIRD_SINGULAR, THIRD_PLURAL]
        spanish  || [FIRST_SINGULAR, FIRST_PLURAL, SECOND_SINGULAR, SECOND_PLURAL, THIRD_SINGULAR, THIRD_PLURAL]
        russian  || [FIRST_SINGULAR, FIRST_PLURAL, SECOND_SINGULAR, SECOND_PLURAL, THIRD_SINGULAR, THIRD_PLURAL]
    }

    @Unroll
    def "In #language.getClass().simpleName, #declension is mapped to '#expectedDeclensionMapping'"() {
        when: 'the declension mapping is requested'
        def actualDeclensionMapping = language.getDeclensionMapping(declension)

        then:
        actualDeclensionMapping == expectedDeclensionMapping

        where: 'the parameters are'
        language | declension     || expectedDeclensionMapping
        english  | FIRST_SINGULAR || 'I'
        english  | FIRST_PLURAL   || 'we'
        english  | SECOND         || 'you'
        english  | THIRD_SINGULAR || 'he, she, it'
        english  | THIRD_PLURAL   || 'they'
        english  | SECOND_PLURAL  || ''
        //
        spanish  | FIRST_SINGULAR  || 'yo'
        spanish  | FIRST_PLURAL    || 'nosotros'
        spanish  | SECOND_SINGULAR || 'tú'
        spanish  | SECOND_PLURAL   || 'vosotros'
        spanish  | THIRD_SINGULAR  || 'él, ella, Usted'
        spanish  | THIRD_PLURAL    || 'ellos, ellas, Ustedes'
        //
        russian  | FIRST_SINGULAR  || 'я'
        russian  | FIRST_PLURAL    || 'мы'
        russian  | SECOND_SINGULAR || 'ты'
        russian  | SECOND_PLURAL   || 'вы'
        russian  | THIRD_SINGULAR  || 'он, она, оно'
        russian  | THIRD_PLURAL    || 'они'
    }

    @Unroll
    def "Test #pos forms in #language.getClass().simpleName"() {
        expect: 'part of speech forms to be returned in order'
        language.getForms(pos) == expectedForms

        where: 'the parameters are'
        language | pos  | expectedForms
        english  | NOUN | [NOMINATIVE, POSSESSIVE]
        //
        spanish  | NOUN | [NOMINATIVE]
        //
        russian  | NOUN | [NOMINATIVE, GENITIVE, DATIVE, ACCUSATIVE, INSTRUMENTAL, PREPOSITIONAL]
    }

    @Unroll
    def "In #language.getClass().simpleName, #form is '#formNameInLanguage'"() {
        when: 'the form name is requested'
        def actualFormName = language.getFormName(form)

        then: 'the form name in the target language is returned'
        actualFormName == formNameInLanguage

        where: 'the parameters are'
        language | form       || formNameInLanguage
        english  | NOMINATIVE || 'nominative case'
        english  | POSSESSIVE || 'possessive case'
        //
        russian  | NOMINATIVE    || 'именительный падеж'
        russian  | GENITIVE      || 'родительный падеж'
        russian  | DATIVE        || 'дательный падеж'
        russian  | ACCUSATIVE    || 'винительный падеж'
        russian  | INSTRUMENTAL  || 'творительный падеж'
        russian  | PREPOSITIONAL || 'предложный падеж'
        //
        spanish  | NOMINATIVE || 'caso nominativo'
    }

    @Unroll
    def "Test genders in #language.getClass().simpleName"() {
        expect: 'genders to be returned in order'
        language.genders == expectedGenders

        where: 'the parameters are'
        language || expectedGenders
        english  || []
        spanish  || [MASCULINE, FEMININE]
        russian  || [MASCULINE, FEMININE, NEUTRAL]
    }

    @Unroll
    def "In #language.getClass().simpleName, #gender is #genderNameInLaguage"() {
        when: 'the gender name is requested'
        def actualGenderName = language.getGenderName(gender)

        then: 'the gender encoding in the target language is returned'
        actualGenderName == genderNameInLaguage

        where: 'the parameters are'
        language | gender    || genderNameInLaguage
        spanish  | MASCULINE || 'm'
        spanish  | FEMININE  || 'f'
        //
        russian  | MASCULINE || 'м'
        russian  | FEMININE  || 'ж'
        russian  | NEUTRAL   || 'ср'
    }
}
