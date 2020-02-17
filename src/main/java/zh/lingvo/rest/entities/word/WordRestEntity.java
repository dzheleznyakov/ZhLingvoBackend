package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.words.PosBlock;
import zh.lingvo.domain.words.SemanticBlock;
import zh.lingvo.domain.words.Transcription;
import zh.lingvo.domain.words.Word;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;

public class WordRestEntity implements JsonEntity {
    private String id;

    private String word;

    private List<String> transcriptions;

    private List<? extends List<SemanticBlockRestEntity>> semanticBlocks;

    public WordRestEntity() {
    }

    public WordRestEntity(Word word, Language language) {
        this.id = word.getId().toString();
        this.word = word.getName().getValue();
        this.transcriptions = CollectionUtils.transform(word::getTranscriptions, Transcription::getIpa);
        List<SemanticBlock> semanticBlocks = word.getSemanticBlocks() == null
                ? ImmutableList.of() : word.getSemanticBlocks();
        this.semanticBlocks = semanticBlocks.stream()
                .map(semanticGroup -> firstNonNull(semanticGroup.getPosBlocks(), ImmutableList.<PosBlock>of()).stream()
                        .map(semanticBlock -> new SemanticBlockRestEntity(semanticBlock, language))
                        .collect(ImmutableList.toImmutableList()))
                .collect(ImmutableList.toImmutableList());
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
        this.semanticBlocks = CollectionUtils.toImmutableList(semanticBlocks);
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
