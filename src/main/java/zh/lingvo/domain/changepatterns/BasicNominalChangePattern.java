package zh.lingvo.domain.changepatterns;

import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.Declension;
import zh.lingvo.domain.Gender;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.util.Pair;
import zh.lingvo.domain.Number;

import java.util.List;

public class BasicNominalChangePattern implements ChangePattern {
    private ImmutableList<Pair<Declension, String>> declensions;
    private ImmutableList<Pair<Number, String>> numbers;
    private ImmutableList<Pair<Gender, String>> genders;

    public BasicNominalChangePattern() {
    }

    public BasicNominalChangePattern(Language language) {
        this(language, false);
    }

    public BasicNominalChangePattern(Language language, boolean withGenders) {
        declensions = language.getDeclensions().stream()
                .map(declension -> Pair.from(declension, language.getDeclensionMapping(declension)))
                .collect(ImmutableList.toImmutableList());
        numbers = language.getNumbers().stream()
                .map(number -> Pair.from(number, language.getNumberName(number)))
                .collect(ImmutableList.toImmutableList());
        genders = withGenders
                ? language.getGenders().stream()
                        .map(gender -> Pair.from(gender, language.getGenderName(gender)))
                        .collect(ImmutableList.toImmutableList())
                : ImmutableList.of();
    }

    public List<Pair<Declension, String>> getDeclensions() {
        return declensions;
    }

    public void setDeclensions(List<Pair<Declension, String>> declensions) {
        this.declensions = ImmutableList.copyOf(declensions);
    }

    public ImmutableList<Pair<Number, String>> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Pair<Number, String>> numbers) {
        this.numbers = ImmutableList.copyOf(numbers);
    }
}
