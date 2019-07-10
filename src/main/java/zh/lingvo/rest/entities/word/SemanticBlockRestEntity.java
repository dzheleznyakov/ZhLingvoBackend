package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.json.Jsonable;

import java.util.List;

public class SemanticBlockRestEntity implements JsonEntity {
    @Jsonable
    private String type;

    @Jsonable
    private List<MeaningRestEntity> meanings;

    public SemanticBlockRestEntity() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
