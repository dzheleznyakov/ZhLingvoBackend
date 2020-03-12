package zh.lingvo.rest.entities.forms;

import zh.lingvo.domain.linguisticcategories.LinguisticCategory;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.Pair;

import java.util.List;

public class ChangeModelDimensionEntity<E extends LinguisticCategory> implements JsonEntity {
    private List<Pair<E, String>> dimensionValues;

    public ChangeModelDimensionEntity() {
    }

    public ChangeModelDimensionEntity(List<Pair<E, String>> dimensionValues) {
        this.dimensionValues = dimensionValues;
    }

    public List<Pair<E, String>> getDimensionValues() {
        return dimensionValues;
    }

    public void setDimensionValues(List<Pair<E, String>> dimensionValues) {
        this.dimensionValues = dimensionValues;
    }
}
