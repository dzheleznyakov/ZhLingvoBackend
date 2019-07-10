package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;

public class Example {
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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("expression", expression)
                .add("explanation", explanation)
                .add("remark", remark)
                .omitNullValues()
                .toString();
    }
}
