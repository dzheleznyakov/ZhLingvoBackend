package zh.lingvo.domain.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.linguisticcategories.Gender;
import zh.lingvo.domain.linguisticcategories.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.linguisticcategories.NounCase;
import zh.lingvo.domain.forms.WordFormCategory;

public class Russian extends Language {
    private static final Russian INSTANCE = new Russian();

    private Russian() {
        super("Ru", "Русский");
    }

    @Override
    protected void loadPosNamings() {
        posNamings = ImmutableMap.<PartOfSpeech, String>builder()
                .put(PartOfSpeech.NOUN, "сущ")
                .put(PartOfSpeech.VERB, "гл")
                .put(PartOfSpeech.ADJECTIVE, "прил")
                .build();
    }

    @Override
    protected void loadNumberNamings() {
        numberNamings = ImmutableMap.of(
                Number.SINGULAR, "ед",
                Number.PLURAL, "мн");
    }

    @Override
    protected void loadWordFormsMappings() {
        wordFormsMappings = ImmutableMap.of(
                PartOfSpeech.NOUN, ImmutableList.of(
                        NounCase.NOMINATIVE,
                        NounCase.GENITIVE,
                        NounCase.DATIVE,
                        NounCase.ACCUSATIVE,
                        NounCase.INSTRUMENTAL,
                        NounCase.PREPOSITIONAL
                )
        );
    }

    @Override
    protected void loadWordFormNamings() {
        wordFormNamings = ImmutableMap.<WordFormCategory, String>builder()
                .put(NounCase.NOMINATIVE, "именительный падеж")
                .put(NounCase.GENITIVE, "родительный падеж")
                .put(NounCase.DATIVE, "дательный падеж")
                .put(NounCase.ACCUSATIVE, "винительный падеж")
                .put(NounCase.INSTRUMENTAL, "творительный падеж")
                .put(NounCase.PREPOSITIONAL, "предложный падеж")
                .build();
    }

    @Override
    protected void loadGenderNamings() {
        genderNamings = ImmutableMap.of(
                Gender.MASCULINE, "м",
                Gender.FEMININE, "ж",
                Gender.NEUTRAL, "ср"
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
                .build();
    }

    public static Russian getInstance() {
        return INSTANCE;
    }
}
