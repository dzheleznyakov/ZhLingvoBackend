package zh.lingvo.persistence.xml.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.SemanticBlock;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.persistence.xml.entities.XmlEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "semanticBlock")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SemanticBlockXmlEntity implements XmlEntity {
    private List<PartOfSpeechBlockXmlEntity> partOfSpeechBlocks;

    public SemanticBlockXmlEntity() {
    }

    public SemanticBlockXmlEntity(SemanticBlock semanticBlock) {
        partOfSpeechBlocks = CollectionUtils.transform(semanticBlock::getPartOfSpeechBlocks, PartOfSpeechBlockXmlEntity::new);
    }

    @XmlElementWrapper(name = "partOfSpeechBlocks")
    @XmlElement(name = "partOfSpeechBlock")
    public List<PartOfSpeechBlockXmlEntity> getPartOfSpeechBlocks() {
        return partOfSpeechBlocks;
    }

    public void setPartOfSpeechBlocks(List<PartOfSpeechBlockXmlEntity> partOfSpeechBlocks) {
        this.partOfSpeechBlocks = partOfSpeechBlocks;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("partOfSpeechBlocks", partOfSpeechBlocks)
                .toString();
    }
}
