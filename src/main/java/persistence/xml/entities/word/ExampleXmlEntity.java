package persistence.xml.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.Example;
import persistence.xml.entities.XmlEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "example")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ExampleXmlEntity implements XmlEntity {
    private String expression;
    private String explanation;
    private String remark;

    public ExampleXmlEntity() {
    }

    public ExampleXmlEntity(Example example) {
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
