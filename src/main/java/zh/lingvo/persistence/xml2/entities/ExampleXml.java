package zh.lingvo.persistence.xml2.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import zh.lingvo.persistence.xml.entities.XmlEntity;

@JacksonXmlRootElement(localName = "example")
public class ExampleXml implements XmlEntity {
    @JacksonXmlProperty(isAttribute = true)
    private String expression;

    @JacksonXmlProperty(isAttribute = true)
    private String explanation;

    @JacksonXmlProperty(isAttribute = true)
    private String remark;

    public ExampleXml() {
    }

    public ExampleXml(String expression, String explanation, String remark) {
        this.expression = expression;
        this.explanation = explanation;
        this.remark = remark;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleXml that = (ExampleXml) o;
        return Objects.equal(expression, that.expression) &&
                Objects.equal(explanation, that.explanation) &&
                Objects.equal(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression, explanation, remark);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("expression", expression)
                .add("explanation", explanation)
                .add("remark", remark)
                .toString();
    }
}
