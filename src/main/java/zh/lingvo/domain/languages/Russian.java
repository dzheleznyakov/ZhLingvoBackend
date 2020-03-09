package zh.lingvo.domain.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.Declension;
import zh.lingvo.domain.Gender;
import zh.lingvo.domain.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.forms.NounWordFormCategory;
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
                        NounWordFormCategory.NOMINATIVE,
                        NounWordFormCategory.GENITIVE,
                        NounWordFormCategory.DATIVE,
                        NounWordFormCategory.ACCUSATIVE,
                        NounWordFormCategory.INSTRUMENTAL,
                        NounWordFormCategory.PREPOSITIONAL
                )
        );
    }

    @Override
    protected void loadWordFormNamings() {
        wordFormNamings = ImmutableMap.<WordFormCategory, String>builder()
                .put(NounWordFormCategory.NOMINATIVE, "именительный падеж")
                .put(NounWordFormCategory.GENITIVE, "родительный падеж")
                .put(NounWordFormCategory.DATIVE, "дательный падеж")
                .put(NounWordFormCategory.ACCUSATIVE, "винительный падеж")
                .put(NounWordFormCategory.INSTRUMENTAL, "творительный падеж")
                .put(NounWordFormCategory.PREPOSITIONAL, "предложный падеж")
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

    @Override
    protected void loadWordFormHelpers() {
        wordFormHelpers = ImmutableMap.<PartOfSpeech, WordFormsHelper>builder()
                .build();
    }

    public static Russian getInstance() {
        return INSTANCE;
    }
}
