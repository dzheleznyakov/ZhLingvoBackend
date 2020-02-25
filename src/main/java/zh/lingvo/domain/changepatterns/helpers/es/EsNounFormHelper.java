package zh.lingvo.domain.changepatterns.helpers.es;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import zh.lingvo.domain.LinguisticCategory;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.words.Name;
import zh.lingvo.domain.words.Word;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static zh.lingvo.domain.NounLinguisticCategories.PLURAL_NOMINATIVE;
import static zh.lingvo.domain.NounLinguisticCategories.SINGULAR_NOMINATIVE;

public class EsNounFormHelper implements WordFormsHelper {
    private static final Set<LinguisticCategory[]> forms = ImmutableSet.of(SINGULAR_NOMINATIVE, PLURAL_NOMINATIVE);
    private static final Set<String> VOWELS = ImmutableSet.of("e", "a", "o", "i", "u", "y",
            "é", "á", "ó", "í", "ú", "ü");

    @Override
    public Map<LinguisticCategory[], String> getForms(Word word, List<Name> formExceptions) {
        String baseForm = word.getName().getValue();
        return ImmutableMap.of(
                SINGULAR_NOMINATIVE, baseForm,
                PLURAL_NOMINATIVE, appendS(baseForm));
    }

    private String appendS(String word) {
        if (endsWithVowel(word))
            return substitute(word, 0, "s");
        if (endsWithIon(word))
            return substitute(word, 2, "ones");
        if (endsWithZ(word))
            return substitute(word, 1, "ces");
        return substitute(word, 0, "es");
    }

    private boolean endsWithVowel(String word) {
        return endingIsInSet(word, VOWELS);
    }

    private boolean endsWithIon(String word) {
        return word.endsWith("ción") || word.endsWith("sión");
    }

    private boolean endsWithZ(String word) {
        return word.endsWith("z");
    }

    @Override
    public Set<LinguisticCategory[]> getLanguageForms() {
        return EsNounFormHelper.forms;
    }
}
