package zh.lingvo.domain.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.linguisticcategories.Gender;
import zh.lingvo.domain.linguisticcategories.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.changepatterns.helpers.es.EsNounFormHelper;
import zh.lingvo.domain.linguisticcategories.NounCase;

public class Spanish extends Language {
    private static final Spanish INSTANCE = new Spanish();

    private Spanish() {
        super("Es", "Español");
    }

    @Override
    protected void loadPosNamings() {
        posNamings = ImmutableMap.<PartOfSpeech, String>builder()
                .put(PartOfSpeech.NOUN, "nombre")
                .put(PartOfSpeech.VERB, "verbo")
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
                PartOfSpeech.NOUN, ImmutableList.of(NounCase.NOMINATIVE)
        );
    }

    @Override
    protected void loadWordFormNamings() {
        wordFormNamings = ImmutableMap.of(NounCase.NOMINATIVE, "caso nominativo");
    }

    @Override
    protected void loadGenderNamings() {
        genderNamings = ImmutableMap.of(
                Gender.FEMININE, "f",
                Gender.MASCULINE, "m"
        );
    }

    @Override
    protected void loadPersonEncodings() {
        personEncodings = ImmutableMap.of();
    }

    @Override
    protected void loadConjugationEncodings() {
        conjugationEncodings = ImmutableMap.of();
    }

    @Override
    protected void loadWordFormHelpers() {
        wordFormHelpers = ImmutableMap.<PartOfSpeech, WordFormsHelper>builder()
                .put(PartOfSpeech.NOUN, new EsNounFormHelper())
                .build();
    }

    public static Spanish getInstance() {
        return INSTANCE;
    }
}
