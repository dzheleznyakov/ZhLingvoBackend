package zh.lingvo.persistence.xml.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.SemanticGroup;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.persistence.xml.entities.XmlEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "semanticGroup")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SemanticGroupXmlEntity implements XmlEntity {
    private List<SemanticBlockXmlEntity> semanticBlocks;

    public SemanticGroupXmlEntity() {
    }

    public SemanticGroupXmlEntity(SemanticGroup semanticGroup) {
        semanticBlocks = CollectionUtils.transform(semanticGroup::getSemanticBlocks, SemanticBlockXmlEntity::new);
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
                .add("semanticBlocks", semanticBlocks)
                .toString();
    }
}
