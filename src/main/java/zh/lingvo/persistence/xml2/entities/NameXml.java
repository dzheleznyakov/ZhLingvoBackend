package zh.lingvo.persistence.xml2.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import zh.lingvo.persistence.xml.entities.XmlEntity;

@JacksonXmlRootElement(localName = "name")
public class NameXml implements XmlEntity {
    @JacksonXmlProperty(isAttribute = true)
    private String value;

    @JacksonXmlProperty(isAttribute = true)
    private String form;

    public NameXml() {
    }

    public NameXml(String value, String form) {
        this.value = value;
        this.form = form;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameXml nameXml = (NameXml) o;
        return Objects.equal(value, nameXml.value) &&
                Objects.equal(form, nameXml.form);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, form);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("value", value)
                .add("form", form)
                .toString();
    }
}
