package zh.lingvo.persistence.xml.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.Word;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.persistence.xml.entities.XmlEntity;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@XmlRootElement(name = "word")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class WordXmlEntity implements XmlEntity {
    private UUID id;
    private String word;
    private List<String> transcriptions;
    private List<SemanticBlockXmlEntity> semanticBlocks;

    public WordXmlEntity() {
    }

    public WordXmlEntity(Word word) {
        this.id = word.getId();
        this.word = word.getName();
        this.transcriptions = word.getTranscriptions();
        this.semanticBlocks = CollectionUtils.transform(word::getSemanticBlocks, SemanticBlockXmlEntity::new);
    }

    @XmlAttribute
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @XmlElementWrapper(name = "transcriptions")
    @XmlElement(name = "transcription")
    public List<String> getTranscriptions() {
        return transcriptions;
    }

    public void setTranscriptions(List<String> transcriptions) {
        this.transcriptions = transcriptions;
    }

    @XmlElementWrapper(name = "semanticBlocks")
    @XmlElement(name = "semanticBlock")
    public List<SemanticBlockXmlEntity> getSemanticBlocks() {
        return semanticBlocks;
    }

    public void setSemanticBlocks(List<SemanticBlockXmlEntity> semanticBlocks) {
        this.semanticBlocks = semanticBlocks;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("word", word)
                .add("transcriptions", transcriptions)
                .add("semanticBlocks", semanticBlocks)
                .toString();
    }
}
