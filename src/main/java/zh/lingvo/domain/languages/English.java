package zh.lingvo.domain.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.Declension;
import zh.lingvo.domain.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.changepatterns.helpers.en.EnNounFormHelper;
import zh.lingvo.domain.forms.NounWordFormCategory;
import zh.lingvo.domain.forms.WordFormCategory;

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
    protected void loadDeclensionMappings() {
        declensionMappings = ImmutableMap.<Declension, String>builder()
                .put(Declension.FIRST_SINGULAR, "I")
                .put(Declension.FIRST_PLURAL, "we")
                .put(Declension.SECOND, "you")
                .put(Declension.THIRD_PLURAL, "they")
                .put(Declension.THIRD_SINGULAR, "he, she, it")
                .build();

    }

    @Override
    protected void loadWordFormsMappings() {
        wordFormsMappings = ImmutableMap.of(
                PartOfSpeech.NOUN, ImmutableList.of(NounWordFormCategory.NOMINATIVE, NounWordFormCategory.POSSESSIVE)
        );
    }

    @Override
    protected void loadWordFormNamings() {
        wordFormNamings = ImmutableMap.<WordFormCategory, String>builder()
                .put(NounWordFormCategory.NOMINATIVE, "nominative case")
                .put(NounWordFormCategory.POSSESSIVE, "possessive case")
                .build();
    }

    @Override
    protected void loadGenderNamings() {
        gendersNamings = ImmutableMap.of();
    }

    @Override
    protected void loadWordFormHelpers() {
        wordFormHelpers = ImmutableMap.<PartOfSpeech, WordFormsHelper>builder()
                .put(PartOfSpeech.NOUN, new EnNounFormHelper())
                .build();
    }

    public static English getInstance() {
        return INSTANCE;
    }
}
