package zh.lingvo.persistence.xml2.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import zh.lingvo.persistence.xml.entities.XmlEntity;

@JacksonXmlRootElement(localName = "translation")
public class TranslationXml implements XmlEntity {
    @JacksonXmlProperty(isAttribute = true)
    private String value;

    @JacksonXmlProperty(isAttribute = true)
    private String elaboration;

    public TranslationXml() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getElaboration() {
        return elaboration;
    }

    public void setElaboration(String elaboration) {
        this.elaboration = elaboration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslationXml that = (TranslationXml) o;
        return Objects.equal(value, that.value) &&
                Objects.equal(elaboration, that.elaboration);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, elaboration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("value", value)
                .add("elaboration", elaboration)
                .toString();
    }
}
