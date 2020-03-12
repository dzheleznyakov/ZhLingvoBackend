package zh.lingvo.rest.entities.forms;

import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.linguisticcategories.LinguisticCategory;
import zh.lingvo.domain.changepatterns.BasicNounChangeModel;
import zh.lingvo.domain.changepatterns.ChangeModel;
import zh.lingvo.rest.entities.JsonEntity;

import java.util.List;

public class ChangeModelEntity implements JsonEntity {
    private List<ChangeModelDimensionEntity<? extends LinguisticCategory>> dimensions;

    public ChangeModelEntity() {
    }

    public ChangeModelEntity(ChangeModel changeModel) {
        if (changeModel instanceof BasicNounChangeModel) setUpBasicNounChangeModel((BasicNounChangeModel) changeModel);
        else dimensions = ImmutableList.of();
    }

    private void setUpBasicNounChangeModel(BasicNounChangeModel changeModel) {
        dimensions = ImmutableList.of(
                new ChangeModelDimensionEntity<>(changeModel.getNumbers()),
                new ChangeModelDimensionEntity<>(changeModel.getCases()));
    }

    public List<ChangeModelDimensionEntity<? extends LinguisticCategory>> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<ChangeModelDimensionEntity<? extends LinguisticCategory>> dimensions) {
        this.dimensions = dimensions;
    }
}
