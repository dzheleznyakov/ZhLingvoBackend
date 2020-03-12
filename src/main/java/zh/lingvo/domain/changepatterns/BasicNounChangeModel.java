package zh.lingvo.domain.changepatterns;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.linguisticcategories.Number;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.forms.WordFormCategory;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.util.Pair;

import java.util.List;

public class BasicNounChangeModel implements ChangeModel {
    private List<Pair<Number, String>> numbers;
    private List<Pair<WordFormCategory, String>> cases;

    public BasicNounChangeModel() {
    }

    public BasicNounChangeModel(Language language) {
        cases = language.getForms(PartOfSpeech.NOUN)
                .stream()
                .map(wordForm -> Pair.from(wordForm, language.getFormName(wordForm)))
                .collect(ImmutableList.toImmutableList());
        numbers = language.getNumbers().stream()
                .map(number -> Pair.from(number, language.getNumberName(number)))
                .collect(ImmutableList.toImmutableList());
    }

    public List<Pair<WordFormCategory, String>> getCases() {
        return cases;
    }

    public void setCases(List<Pair<WordFormCategory, String>> cases) {
        this.cases = ImmutableList.copyOf(cases);
    }

    public List<Pair<Number, String>> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Pair<Number, String>> numbers) {
        this.numbers = ImmutableList.copyOf(numbers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicNounChangeModel that = (BasicNounChangeModel) o;
        return Objects.equal(numbers, that.numbers) &&
                Objects.equal(cases, that.cases);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numbers, cases);
    }
}
