package zh.lingvo.domain.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.Declension;
import zh.lingvo.domain.Gender;
import zh.lingvo.domain.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.forms.NounWordForm;

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
    protected void loadDeclensionMappings() {
        declensionMappings = ImmutableMap.<Declension, String>builder()
                .put(Declension.FIRST_SINGULAR, "yo")
                .put(Declension.FIRST_PLURAL, "nosotros")
                .put(Declension.SECOND_SINGULAR, "tú")
                .put(Declension.SECOND_PLURAL, "vosotros")
                .put(Declension.THIRD_SINGULAR, "él, ella, Usted")
                .put(Declension.THIRD_PLURAL, "ellos, ellas, Ustedes")
                .build();
    }

    @Override
    protected void loadWordFormsMappings() {
        wordFormsMappings = ImmutableMap.of(
                PartOfSpeech.NOUN, ImmutableList.of(NounWordForm.NOMINATIVE)
        );
    }

    @Override
    protected void loadWordFormNamings() {
        wordFormNamings = ImmutableMap.of(NounWordForm.NOMINATIVE, "caso nominativo");
    }

    @Override
    protected void loadGenderNamings() {
        gendersNamings = ImmutableMap.of(
                Gender.FEMININE, "f",
                Gender.MASCULINE, "m"
        );
    }

    @Override
    protected void loadWordFormHelpers() {
        wordFormHelpers = ImmutableMap.<PartOfSpeech, WordFormsHelper>builder()
                .build();
    }

    public static Spanish getInstance() {
        return INSTANCE;
    }
}
