package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.Example;
import zh.lingvo.rest.entities.JsonEntity;

public class ExampleRestEntity implements JsonEntity {
    private String expression;

    private String explanation;

    private String remark;

    public ExampleRestEntity() {
    }

    public ExampleRestEntity(Example example) {
        expression = example.getExpression();
        explanation = example.getExplanation();
        remark = example.getRemark();
    }

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
