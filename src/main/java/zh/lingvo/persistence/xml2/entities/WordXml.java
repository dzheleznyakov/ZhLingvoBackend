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

@JacksonXmlRootElement(localName = "word")
@JsonInclude(Include.NON_NULL)
public class WordXml implements XmlEntity {
    @JacksonXmlProperty(isAttribute = true)
    private String id;

    private NameXml name;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "transcription")
    private List<TranscriptionXml> transcriptions;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "semanticBlock")
    private List<SemanticBlockXml> semanticBlocks;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "formException")
    private List<FormExceptionXml> formExceptions;

    public WordXml() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NameXml getName() {
        return name;
    }

    public void setName(NameXml name) {
        this.name = name;
    }

    public List<TranscriptionXml> getTranscriptions() {
        return transcriptions;
    }

    public void setTranscriptions(List<TranscriptionXml> transcriptions) {
        this.transcriptions = transcriptions;
    }

    public List<SemanticBlockXml> getSemanticBlocks() {
        return semanticBlocks;
    }

    public void setSemanticBlocks(List<SemanticBlockXml> semanticBlocks) {
        this.semanticBlocks = semanticBlocks;
    }

    public List<FormExceptionXml> getFormExceptions() {
        return formExceptions;
    }

    public void setFormExceptions(List<FormExceptionXml> formExceptions) {
        this.formExceptions = formExceptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordXml wordXml = (WordXml) o;
        return Objects.equal(id, wordXml.id) &&
                Objects.equal(name, wordXml.name) &&
                Objects.equal(transcriptions, wordXml.transcriptions) &&
                Objects.equal(semanticBlocks, wordXml.semanticBlocks) &&
                Objects.equal(formExceptions, wordXml.formExceptions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, transcriptions, semanticBlocks, formExceptions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("transcriptions", transcriptions)
                .add("semanticBlocks", semanticBlocks)
                .add("formExceptions", formExceptions)
                .toString();
    }
}
