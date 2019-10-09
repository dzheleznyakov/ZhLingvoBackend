package zh.lingvo.domain.changepatterns;

import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.Gender;
import zh.lingvo.domain.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.forms.WordForm;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.util.Pair;

import java.util.List;

public class BasicNounChangeModel implements ChangeModel {
    private ImmutableList<Pair<Number, String>> numbers;
    private ImmutableList<Pair<WordForm, String>> cases;
    private ImmutableList<Pair<Gender, String>> genders;

    public BasicNounChangeModel() {
    }

    public BasicNounChangeModel(Language language) {
        this(language, false);
    }

    public BasicNounChangeModel(Language language, boolean withGenders) {
        cases = language.getForms(PartOfSpeech.NOUN)
                .stream()
                .map(wordForm -> Pair.from(wordForm, language.getFormName(wordForm)))
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

    public List<Pair<WordForm, String>> getCases() {
        return cases;
    }

    public void setCases(List<Pair<WordForm, String>> cases) {
        this.cases = ImmutableList.copyOf(cases);
    }

    public ImmutableList<Pair<Number, String>> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Pair<Number, String>> numbers) {
        this.numbers = ImmutableList.copyOf(numbers);
    }
}
