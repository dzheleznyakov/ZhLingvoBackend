package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.json.Jsonable;

import java.util.List;

public class WordRestEntity implements JsonEntity {
    @Jsonable
    private String id;

    @Jsonable
    private String word;

    @Jsonable
    private List<String> transcriptions;

    @Jsonable
    private List<? extends List<SemanticBlockRestEntity>> semanticBlocks;

    public WordRestEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getTranscriptions() {
        return transcriptions;
    }

    public void setTranscriptions(List<String> transcriptions) {
        this.transcriptions = transcriptions;
    }

    public List<? extends List<SemanticBlockRestEntity>> getSemanticBlocks() {
        return semanticBlocks;
    }

    public void setSemanticBlocks(List<? extends List<SemanticBlockRestEntity>> semanticBlocks) {
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
