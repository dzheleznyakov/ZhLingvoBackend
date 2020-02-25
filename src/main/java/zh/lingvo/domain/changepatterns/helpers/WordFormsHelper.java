package zh.lingvo.domain.changepatterns.helpers;

import com.google.common.base.Preconditions;
import zh.lingvo.domain.LinguisticCategory;
import zh.lingvo.domain.words.Name;
import zh.lingvo.domain.words.Word;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public interface WordFormsHelper {
    Map<LinguisticCategory[], String> getForms(Word word, List<Name> formExceptions);

    Set<LinguisticCategory[]> getLanguageForms();

    default LinguisticCategory[] getForm(String formName) {
        String[] parts = formName.split(";");
        if (parts.length != 2)
            throw new IllegalArgumentException(String.format("Form name for English noun should have 2 parts, found %d", parts.length));
        return getLanguageForms().stream()
                .filter(form -> Objects.equals(form[0].name(), parts[0]) && Objects.equals(form[1].name(), parts[1]))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Illegal form name for English noun: [%s]", formName)));
    }

    default String substitute(String word, int backOffset, String suffix) {
        Preconditions.checkArgument(backOffset >= 0,
                "backOffset [%d] should be greater or equal to 0", backOffset);
        Preconditions.checkArgument(backOffset <= word.length(),
                "backOffset [%d] should be less or equal than the length of [%s]", backOffset, word);
        return word.substring(0, word.length() - backOffset) + suffix;
    }

    default boolean endingIsInSet(String word, Set<String> set) {
        return set.stream().anyMatch(word::endsWith);
    }
}
