package zh.lingvo.persistence.xml.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.words.PosBlock;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.persistence.xml.entities.XmlEntity;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "partOfSpeechBlock")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PartOfSpeechBlockXmlEntity implements XmlEntity {
    private PartOfSpeech partOfSpeech;
    private List<MeaningXmlEntity> meanings;

    public PartOfSpeechBlockXmlEntity() {
    }

    public PartOfSpeechBlockXmlEntity(PosBlock posBlock) {
        partOfSpeech = posBlock.getPos();
        meanings = CollectionUtils.transform(posBlock::getMeanings, MeaningXmlEntity::new);
    }

    @XmlAttribute
    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    @XmlElementWrapper(name = "meanings")
    @XmlElement(name = "meaning")
    public List<MeaningXmlEntity> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<MeaningXmlEntity> meanings) {
        this.meanings = meanings;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("partOfSpeech", partOfSpeech)
                .add("#OfMeenings", meanings == null ? null : meanings.size())
                .toString();
    }
}
