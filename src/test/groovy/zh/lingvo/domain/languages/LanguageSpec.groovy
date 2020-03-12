package zh.lingvo.domain.languages

import spock.lang.Specification
import spock.lang.Unroll
import zh.lingvo.domain.linguisticcategories.Person

import static zh.lingvo.domain.PartOfSpeech.ADJECTIVE
import static zh.lingvo.domain.PartOfSpeech.NOUN
import static zh.lingvo.domain.PartOfSpeech.VERB
import static zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_FIRST
import static zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_SECOND
import static zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_THIRD
import static zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_FIRST
import static zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_SECOND
import static zh.lingvo.domain.forms.VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_THIRD
import static zh.lingvo.domain.linguisticcategories.Gender.FEMININE
import static zh.lingvo.domain.linguisticcategories.Gender.MASCULINE
import static zh.lingvo.domain.linguisticcategories.Gender.NEUTRAL
import static zh.lingvo.domain.linguisticcategories.NounCase.ACCUSATIVE
import static zh.lingvo.domain.linguisticcategories.NounCase.DATIVE
import static zh.lingvo.domain.linguisticcategories.NounCase.GENITIVE
import static zh.lingvo.domain.linguisticcategories.NounCase.INSTRUMENTAL
import static zh.lingvo.domain.linguisticcategories.NounCase.NOMINATIVE
import static zh.lingvo.domain.linguisticcategories.NounCase.POSSESSIVE
import static zh.lingvo.domain.linguisticcategories.NounCase.PREPOSITIONAL
import static zh.lingvo.domain.linguisticcategories.Number.PLURAL
import static zh.lingvo.domain.linguisticcategories.Number.SINGULAR

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

        then: 'the short form of the number in the target language is returned'
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
    def "In #language.getClass().simpleName, #person is '#expectedEncoding'"() {
        when: 'the person encoding is requested'
        def actualEncoding = language.getPersonEncoding(person)

        then: 'the returned encoding in the target language is correct'
        actualEncoding == expectedEncoding

        where: 'the parameters are'
        language | person        || expectedEncoding
        english  | Person.FIRST  || '1st person'
        english  | Person.SECOND || '2nd person'
        english  | Person.THIRD  || '3rd person'
    }

    @Unroll
    def "In #language.getClass().simpleName, #conjugation is #conjugationEncoding"() {
        when: 'the person name is requested'
        def personEncoding = language.getConjugationEncodings()

        then: 'the the encoding of the person in the target language is returned'
        personEncoding.get(conjugation) == conjugationEncoding

        where: 'the parameters are'
        language | conjugation     || conjugationEncoding
        english  | PRESENT_SIMPLE_SINGULAR_FIRST || 'I'
        english  | PRESENT_SIMPLE_PLURAL_FIRST || 'we'
        english  | PRESENT_SIMPLE_SINGULAR_SECOND || 'you'
        english  | PRESENT_SIMPLE_PLURAL_SECOND || 'you'
        english  | PRESENT_SIMPLE_SINGULAR_THIRD || 'he, she, it'
        english  | PRESENT_SIMPLE_PLURAL_THIRD || 'they'
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
