package zh.lingvo.domain.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.changepatterns.helpers.en.EnNounFormHelper;
import zh.lingvo.domain.changepatterns.helpers.en.EnVerbFormHelper;
import zh.lingvo.domain.forms.VerbConjugationCategory;
import zh.lingvo.domain.forms.WordFormCategory;
import zh.lingvo.domain.linguisticcategories.LinguisticCategory;
import zh.lingvo.domain.linguisticcategories.NounCase;
import zh.lingvo.domain.linguisticcategories.Number;
import zh.lingvo.domain.linguisticcategories.Person;

public class English extends Language {
    private static final English INSTANCE = new English();

    private English() {
        super("En", "English");
    }

    @Override
    protected void loadPosNamings() {
        posNamings = ImmutableMap.<PartOfSpeech, String>builder()
                .put(PartOfSpeech.NOUN, "noun")
                .put(PartOfSpeech.VERB, "verb")
                .put(PartOfSpeech.ADJECTIVE, "adj")
                .build();
    }

    @Override
    protected void loadNumberNamings() {
        numberNamings = ImmutableMap.of(
                Number.SINGULAR, "sing",
                Number.PLURAL, "pl");
    }

    @Override
    protected void loadWordFormsMappings() {
        wordFormsMappings = ImmutableMap.of(
                PartOfSpeech.NOUN, ImmutableList.of(NounCase.NOMINATIVE, NounCase.POSSESSIVE)
        );
    }

    @Override
    protected void loadWordFormNamings() {
        wordFormNamings = ImmutableMap.<WordFormCategory, String>builder()
                .put(NounCase.NOMINATIVE, "nominative case")
                .put(NounCase.POSSESSIVE, "possessive case")
                .build();
    }

    @Override
    protected void loadGenderNamings() {
        genderNamings = ImmutableMap.of();
    }

    @Override
    protected void loadPersonEncodings() {
        personEncodings = ImmutableMap.of(
                Person.FIRST, "1st person",
                Person.SECOND, "2nd person",
                Person.THIRD, "3rd person");
    }

    @Override
    protected void loadConjugationEncodings() {
        conjugationEncodings = ImmutableMap.<LinguisticCategory[], String>builder()
                .put(VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_FIRST, "I")
                .put(VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_FIRST, "we")
                .put(VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_SECOND, "you")
                .put(VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_SECOND, "you")
                .put(VerbConjugationCategory.PRESENT_SIMPLE_SINGULAR_THIRD, "he, she, it")
                .put(VerbConjugationCategory.PRESENT_SIMPLE_PLURAL_THIRD, "they")
                .build();
    }

    @Override
    protected void loadWordFormHelpers() {
        wordFormHelpers = ImmutableMap.<PartOfSpeech, WordFormsHelper>builder()
                .put(PartOfSpeech.NOUN, new EnNounFormHelper())
                .put(PartOfSpeech.VERB, new EnVerbFormHelper())
                .build();
    }

    public static English getInstance() {
        return INSTANCE;
    }
}
