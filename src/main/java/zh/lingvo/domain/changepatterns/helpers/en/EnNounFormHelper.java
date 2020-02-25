package zh.lingvo.domain.changepatterns.helpers.en;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import zh.lingvo.domain.LinguisticCategory;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.words.Name;
import zh.lingvo.domain.words.Word;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static zh.lingvo.domain.NounLinguisticCategories.PLURAL_NOMINATIVE;
import static zh.lingvo.domain.NounLinguisticCategories.PLURAL_POSSESSIVE;
import static zh.lingvo.domain.NounLinguisticCategories.SINGULAR_NOMINATIVE;
import static zh.lingvo.domain.NounLinguisticCategories.SINGULAR_POSSESSIVE;

public class EnNounFormHelper implements WordFormsHelper {
    private static final Set<LinguisticCategory[]> forms = ImmutableSet.of(SINGULAR_NOMINATIVE, PLURAL_NOMINATIVE, SINGULAR_POSSESSIVE, PLURAL_POSSESSIVE);;

    private static final Set<String> SIBILANTS = ImmutableSet.of("s", "sh", "x");
    private static final Set<String> VOWELS = ImmutableSet.of("e", "a", "o", "i", "u", "y");

    @Override
    public Map<LinguisticCategory[], String> getForms(Word word, List<Name> formExceptions) {
        String baseForm = word.getName().getValue();
        String pluralBaseForm = formExceptions == null ? appendS(baseForm) : formExceptions.stream()
                .filter(name -> Arrays.equals(name.getForm(), PLURAL_NOMINATIVE))
                .findAny()
                .map(Name::getValue)
                .orElseGet(() -> appendS(baseForm));
        return ImmutableMap.of(
                SINGULAR_NOMINATIVE, baseForm,
                PLURAL_NOMINATIVE, pluralBaseForm,
                SINGULAR_POSSESSIVE, baseForm + "'s",
                PLURAL_POSSESSIVE, pluralBaseForm.endsWith("s") ? pluralBaseForm + '\'' : pluralBaseForm + "'s"
        );
    }

    private String appendS(String word) {
        if (endsWithSibilant(word))
            return substitute(word, 0, "es");
        if (endsWithDiphthongY(word))
            return substitute(word, 1, "ies");
        if (endsWithShortF(word))
            return substitute(word, 1, "ves");
        if (endsWithShortFE(word))
            return substitute(word, 2, "ves");
        return substitute(word, 0, "s");
    }

    private boolean endsWithSibilant(String word) {
        return endingIsInSet(word, SIBILANTS);
    }

    private boolean endsWithShortF(String word) {
        return word.endsWith("f") && endingIsInSet(word.substring(0, word.length() - 1), VOWELS);
    }

    private boolean endsWithShortFE(String word) {
        return word.endsWith("fe") && endingIsInSet(word.substring(0, word.length() - 2), VOWELS);
    }

    private boolean endsWithDiphthongY(String word) {
        return word.endsWith("y") &&
                word.length() > 1 &&
                word.charAt(word.length() - 2) != 'y' &&
                !endingIsInSet(word.substring(0, word.length() - 1), VOWELS);
    }

    @Override
    public Set<LinguisticCategory[]> getLanguageForms() {
        return EnNounFormHelper.forms;
    }
}
