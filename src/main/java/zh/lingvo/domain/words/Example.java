package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class Example implements WordEntity {
    private String expression;
    private String explanation;
    private String remark;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean isVoid() {
        return Strings.isNullOrEmpty(expression)
                && Strings.isNullOrEmpty(explanation)
                && Strings.isNullOrEmpty(remark);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Example example = (Example) o;
        return Objects.equal(expression, example.expression) &&
                Objects.equal(explanation, example.explanation) &&
                Objects.equal(remark, example.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression, explanation, remark);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("expression", expression)
                .add("explanation", explanation)
                .add("remark", remark)
                .omitNullValues()
                .toString();
    }
}
