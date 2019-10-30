package zh.lingvo.persistence.xml2.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import zh.lingvo.persistence.xml.entities.XmlEntity;

import java.util.List;

@JacksonXmlRootElement(localName = "posBlock")
@JsonInclude(Include.NON_NULL)
public class PosBlockXml implements XmlEntity {
    @JacksonXmlProperty(isAttribute = true)
    private String pos;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "meaning")
    private List<MeaningXml> meanings;

    public PosBlockXml() {
    }

    public PosBlockXml(String pos, List<MeaningXml> meanings) {
        this.pos = pos;
        this.meanings = meanings;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public List<MeaningXml> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<MeaningXml> meanings) {
        this.meanings = meanings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosBlockXml that = (PosBlockXml) o;
        return Objects.equal(pos, that.pos) &&
                Objects.equal(meanings, that.meanings);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pos, meanings);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("pos", pos)
                .add("meanings", meanings)
                .toString();
    }
}
