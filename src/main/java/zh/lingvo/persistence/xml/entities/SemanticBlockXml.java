package zh.lingvo.persistence.xml.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

@JacksonXmlRootElement(localName = "semanticBlock")
@JsonInclude(Include.NON_NULL)
public class SemanticBlockXml implements XmlEntity {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "posBlock")
    private List<PosBlockXml> posBlocks;

    public SemanticBlockXml() {
    }

    public List<PosBlockXml> getPosBlocks() {
        return posBlocks;
    }

    public void setPosBlocks(List<PosBlockXml> posBlocks) {
        this.posBlocks = posBlocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemanticBlockXml that = (SemanticBlockXml) o;
        return Objects.equal(posBlocks, that.posBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(posBlocks);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("posBlocks", posBlocks)
                .toString();
    }
}
