package zh.lingvo.domain.languages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.Declension;
import zh.lingvo.domain.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.forms.NounWordForm;
import zh.lingvo.domain.forms.WordForm;

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
                PartOfSpeech.NOUN, ImmutableList.of(NounWordForm.NOMINATIVE, NounWordForm.POSSESSIVE)
        );
    }

    @Override
    protected void loadWordFormNamings() {
        wordFormNamings = ImmutableMap.<WordForm, String>builder()
                .put(NounWordForm.NOMINATIVE, "nominative case")
                .put(NounWordForm.POSSESSIVE, "possessive case")
                .build();
    }

    @Override
    protected void loadGenderNamings() {
        gendersNamings = ImmutableMap.of();
    }

    public static English getInstance() {
        return INSTANCE;
    }
}
