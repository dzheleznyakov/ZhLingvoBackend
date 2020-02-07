package zh.lingvo.domain.words;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import zh.lingvo.domain.LinguisticCategory;

import java.util.stream.IntStream;

public class Name implements WordEntity {
    private String value;

    private LinguisticCategory[] form;

    public Name() {
    }

    public Name(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LinguisticCategory[] getForm() {
        return form;
    }

    public void setForm(LinguisticCategory[] form) {
        this.form = form;
    }

    @Override
    public boolean isVoid() {
        return Strings.isNullOrEmpty(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        if (!Objects.equal(value, name.value)) return false;
        if (form == null && name.form == null) return true;
        if (form == null || name.form == null) return false;
        if (form.length != name.form.length) return false;
        return IntStream.range(0, form.length)
                .allMatch(i -> Objects.equal(form[i], name.form[i]));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, form);
    }
}
