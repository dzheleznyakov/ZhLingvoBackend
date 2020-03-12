package zh.lingvo.domain.changepatterns;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.linguisticcategories.Number;
import zh.lingvo.domain.linguisticcategories.Person;
import zh.lingvo.util.Pair;

import java.util.List;

public class BasicVerbChangeModel implements ChangeModel {
    private List<Pair<Number, String>> numbers;
    private List<Pair<Person, String>> persons;

    public BasicVerbChangeModel() {
    }

    public BasicVerbChangeModel(Language language) {
        numbers = language.getNumbers().stream()
                .map(number -> Pair.from(number, language.getNumberName(number)))
                .collect(ImmutableList.toImmutableList());
        persons = language.getPersonEndodings().entrySet()
                .stream()
                .map(Pair::from)
                .collect(ImmutableList.toImmutableList());
    }

    public List<Pair<Number, String>> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Pair<Number, String>> numbers) {
        this.numbers = ImmutableList.copyOf(numbers);
    }

    public List<Pair<Person, String>> getPersons() {
        return persons;
    }

    public void setPersons(List<Pair<Person, String>> persons) {
        this.persons = ImmutableList.copyOf(persons);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicVerbChangeModel that = (BasicVerbChangeModel) o;
        return Objects.equal(numbers, that.numbers) &&
                Objects.equal(persons, that.persons);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numbers, persons);
    }
}
