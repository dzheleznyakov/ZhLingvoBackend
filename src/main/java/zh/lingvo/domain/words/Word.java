package zh.lingvo.domain.words;

import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

public class Word {
    private final UUID id;
    private final String word;
    private List<String> transcriptions;
    private List<SemanticGroup> semanticGroups;

    public Word(UUID id, String word) {
        this.id = id;
        this.word = word;
    }

    public UUID getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    public List<String> getTranscriptions() {
        return transcriptions;
    }

    public void setTranscriptions(List<String> transcriptions) {
        this.transcriptions = CollectionUtils.toImmutableList(transcriptions);
    }

    public List<SemanticGroup> getSemanticGroups() {
        return semanticGroups;
    }

    public void setSemanticGroups(List<SemanticGroup> semanticGroups) {
        this.semanticGroups = CollectionUtils.toImmutableList(semanticGroups);
    }
}
