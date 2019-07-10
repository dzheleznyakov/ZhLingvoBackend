package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.json.Jsonable;

public class ExampleRestEntity implements JsonEntity {
    @Jsonable
    private String expression;

    @Jsonable
    private String explanation;

    @Jsonable
    private String remark;

    public ExampleRestEntity() {
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
