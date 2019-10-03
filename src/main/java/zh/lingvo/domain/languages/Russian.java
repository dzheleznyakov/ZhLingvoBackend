package zh.lingvo.domain.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.Declension;
import zh.lingvo.domain.Gender;
import zh.lingvo.domain.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.forms.NounWordForm;
import zh.lingvo.domain.forms.WordForm;

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
    protected void loadDeclensionMappings() {
        declensionMappings = ImmutableMap.<Declension, String>builder()
                .put(Declension.FIRST_SINGULAR, "я")
                .put(Declension.FIRST_PLURAL, "мы")
                .put(Declension.SECOND_SINGULAR, "ты")
                .put(Declension.SECOND_PLURAL, "вы")
                .put(Declension.THIRD_SINGULAR, "он, она, оно")
                .put(Declension.THIRD_PLURAL, "они")
                .build();
    }

    @Override
    protected void loadWordFormsMappings() {
        wordFormsMappings = ImmutableMap.of(
                PartOfSpeech.NOUN, ImmutableList.of(
                        NounWordForm.NOMINATIVE,
                        NounWordForm.GENITIVE,
                        NounWordForm.DATIVE,
                        NounWordForm.ACCUSATIVE,
                        NounWordForm.INSTRUMENTAL,
                        NounWordForm.PREPOSITIONAL
                )
        );
    }

    @Override
    protected void loadWordFormNamings() {
        wordFormNamings = ImmutableMap.<WordForm, String>builder()
                .put(NounWordForm.NOMINATIVE, "именительный падеж")
                .put(NounWordForm.GENITIVE, "родительный падеж")
                .put(NounWordForm.DATIVE, "дательный падеж")
                .put(NounWordForm.ACCUSATIVE, "винительный падеж")
                .put(NounWordForm.INSTRUMENTAL, "творительный падеж")
                .put(NounWordForm.PREPOSITIONAL, "предложный падеж")
                .build();
    }

    @Override
    protected void loadGenderNamings() {
        gendersNamings = ImmutableMap.of(
                Gender.MASCULINE, "м",
                Gender.FEMININE, "ж",
                Gender.NEUTRAL, "ср"
        );
    }

    public static Russian getInstance() {
        return INSTANCE;
    }
}
