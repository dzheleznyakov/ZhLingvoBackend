package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.linguisticcategories.Gender;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.words.PosBlock;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class SemanticBlockRestEntity implements JsonEntity {
    private String type;

    private String gender;

    private List<MeaningRestEntity> meanings;

    public SemanticBlockRestEntity() {
    }

    public SemanticBlockRestEntity(PosBlock posBlock, Language language) {
        type = language.getPartsOfSpeechName(posBlock.getPos());
        Gender gender = posBlock.getGender();
        if (gender != null) {
            String genderName = language.getGenderName(gender);
            this.gender = genderName;
        }
        meanings = CollectionUtils.transform(posBlock::getMeanings, MeaningRestEntity::new);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<MeaningRestEntity> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<MeaningRestEntity> meanings) {
        this.meanings = meanings;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("meanings", meanings)
                .omitNullValues()
                .toString();
    }
}
