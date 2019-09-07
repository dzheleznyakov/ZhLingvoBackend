package zh.lingvo.domain.words;

import zh.lingvo.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

public class Word {
    private final UUID id;
    private String name;
    private List<String> transcriptions;
    private List<SemanticBlock> semanticBlocks;

    public Word(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTranscriptions() {
        return transcriptions;
    }

    public void setTranscriptions(List<String> transcriptions) {
        this.transcriptions = CollectionUtils.toImmutableList(transcriptions);
    }

    public List<SemanticBlock> getSemanticBlocks() {
        return semanticBlocks;
    }

    public void setSemanticBlocks(List<SemanticBlock> semanticBlocks) {
        this.semanticBlocks = CollectionUtils.toImmutableList(semanticBlocks);
    }
}
