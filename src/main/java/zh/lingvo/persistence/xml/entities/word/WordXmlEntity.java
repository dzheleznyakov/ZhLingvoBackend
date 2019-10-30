package zh.lingvo.persistence.xml.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.words.Word;
import zh.lingvo.persistence.xml.entities.XmlEntity;
import zh.lingvo.util.CollectionUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@XmlRootElement(name = "word")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class WordXmlEntity implements XmlEntity {
    private UUID id;
    private String word;
    private List<String> transcriptions;
    private List<SemanticBlockXmlEntity> semanticBlocks;
    private Map<PartOfSpeech, Map<Enum<?>, String>> wordFormsExceptions;

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

//    @XmlElementWrapper(name = "wordFormsExceptions")
//    @XmlJavaTypeAdapter(javax.xml.bind.annotation.adapters.XmlAdapter)
//    @XmlElement(name = "exception")
//    public Map<PartOfSpeech, Map<Enum<?>, String>> getWordFormsExceptions() {
//        return wordFormsExceptions;
//    }

    public void setWordFormsExceptions(Map<PartOfSpeech, Map<Enum<?>, String>> wordFormsExceptions) {
        this.wordFormsExceptions = wordFormsExceptions;
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
