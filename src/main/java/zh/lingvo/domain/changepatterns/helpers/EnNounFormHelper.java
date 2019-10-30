package zh.lingvo.domain.changepatterns.helpers;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import zh.lingvo.domain.LinguisticCategory;
import zh.lingvo.domain.Number;
import zh.lingvo.domain.forms.NounWordForm;
import zh.lingvo.domain.words.Word;

import java.util.Map;
import java.util.Set;

public class EnNounFormHelper implements WordFormsHelper {
    static final LinguisticCategory[] SINGULAR_NOMINATIVE = new LinguisticCategory[] { Number.SINGULAR, NounWordForm.NOMINATIVE };
    static final LinguisticCategory[] PLURAL_NOMINATIVE = new LinguisticCategory[] { Number.PLURAL, NounWordForm.NOMINATIVE };
    static final LinguisticCategory[] SINGULAR_POSSESSIVE = new LinguisticCategory[] { Number.SINGULAR, NounWordForm.POSSESSIVE };
    static final LinguisticCategory[] PLURAL_POSSESSIVE = new LinguisticCategory[] { Number.PLURAL, NounWordForm.POSSESSIVE };

    private static final Set<String> SIBILANTS = ImmutableSet.of("s", "sh", "x");
    private static final Set<String> VOCALS = ImmutableSet.of("e", "a", "o", "i", "u", "y");

    @Override
    public Map<LinguisticCategory[], String> getForms(Word word) {
        String baseForm = word.getName();
        String pluralBaseForm = appendS(baseForm);
        return ImmutableMap.of(
                SINGULAR_NOMINATIVE, baseForm,
                PLURAL_NOMINATIVE, pluralBaseForm,
                SINGULAR_POSSESSIVE, baseForm + "'s",
                PLURAL_POSSESSIVE, pluralBaseForm + '\''
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

    private String substitute(String word, int backOffset, String suffix) {
        Preconditions.checkArgument(backOffset >= 0,
                "backOffset [%d] should be greater or equal to 0", backOffset);
        Preconditions.checkArgument(backOffset <= word.length(),
                "backOffset [%d] should be less or equal than the length of [%s]", backOffset, word);
        return word.substring(0, word.length() - backOffset) + suffix;
    }

    private boolean endsWithSibilant(String word) {
        return endingIsInSet(word, SIBILANTS);
    }

    private boolean endsWithShortF(String word) {
        return word.endsWith("f") && endingIsInSet(word.substring(0, word.length() - 1), VOCALS);
    }

    private boolean endsWithShortFE(String word) {
        return word.endsWith("fe") && endingIsInSet(word.substring(0, word.length() - 2), VOCALS);
    }

    private boolean endsWithDiphthongY(String word) {
        return word.endsWith("y") &&
                word.length() > 1 &&
                word.charAt(word.length() - 2) != 'y' &&
                !endingIsInSet(word.substring(0, word.length() - 1), VOCALS);
    }

    private String makePossessive(String wordName) {
        return wordName + "'s";
    }

    private boolean endingIsInSet(String word, Set<String> set) {
        return set.stream().anyMatch(word::endsWith);
    }
}
