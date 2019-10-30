package zh.lingvo.persistence.xml2.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

@JacksonXmlRootElement(localName = "formException")
public class FormExceptionXml {
    @JacksonXmlProperty(isAttribute = true)
    private String pos;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "name")
    private List<NameXml> names;

    public FormExceptionXml() {
    }

    public FormExceptionXml(String pos, List<NameXml> names) {
        this.pos = pos;
        this.names = names;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public List<NameXml> getNames() {
        return names;
    }

    public void setNames(List<NameXml> names) {
        this.names = names;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormExceptionXml that = (FormExceptionXml) o;
        return Objects.equal(pos, that.pos) &&
                Objects.equal(names, that.names);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pos, names);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("pos", pos)
                .add("name", names)
                .toString();
    }
}
