package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class Meaning implements WordEntity {
    private String remark;
    private List<Translation> translations;
    private List<Example> examples;

    public Meaning() {
    }

    public Meaning(List<Translation> translations) {
        this.translations = translations;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = CollectionUtils.toImmutableList(translations);
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = CollectionUtils.toImmutableList(examples);
    }

    @Override
    public boolean isVoid() {
        return Strings.isNullOrEmpty(remark) &&
                CollectionUtils.isNullOrEmpty(translations) &&
                CollectionUtils.isNullOrEmpty(examples);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meaning meaning = (Meaning) o;
        return Objects.equal(remark, meaning.remark) &&
                Objects.equal(translations, meaning.translations) &&
                Objects.equal(examples, meaning.examples);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(remark, translations, examples);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("remark", remark)
                .add("translations", translations)
                .add("examples", examples)
                .toString();
    }
}
