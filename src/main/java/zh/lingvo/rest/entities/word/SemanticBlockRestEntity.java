package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.words.PartOfSpeechBlock;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class SemanticBlockRestEntity implements JsonEntity {
    private String type;

    private List<MeaningRestEntity> meanings;

    public SemanticBlockRestEntity() {
    }

    public SemanticBlockRestEntity(PartOfSpeechBlock partOfSpeechBlock, Language language) {
        type = language.getPartsOfSpeechName(partOfSpeechBlock.getPartOfSpeech());
        meanings = CollectionUtils.transform(partOfSpeechBlock::getMeanings, MeaningRestEntity::new);
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
