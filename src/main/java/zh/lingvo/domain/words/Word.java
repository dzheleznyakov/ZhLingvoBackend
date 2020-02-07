package zh.lingvo.domain.words;

import com.google.common.base.Objects;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Word {
    private final UUID id;
    private Name name;
    private List<Transcription> transcriptions;
    private List<SemanticBlock> semanticBlocks;
    private Map<PartOfSpeech, List<Name>> formExceptions;

    public Word(UUID id, String name) {
        this.id = id;
        this.name = new Name(name);
    }

    public UUID getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public List<Transcription> getTranscriptions() {
        return transcriptions;
    }

    public void setTranscriptions(List<Transcription> transcriptions) {
        this.transcriptions = CollectionUtils.toImmutableList(transcriptions);
    }

    public List<SemanticBlock> getSemanticBlocks() {
        return semanticBlocks;
    }

    public void setSemanticBlocks(List<SemanticBlock> semanticBlocks) {
        this.semanticBlocks = CollectionUtils.toImmutableList(semanticBlocks);
    }

    public Map<PartOfSpeech, List<Name>> getFormExceptions() {
        return formExceptions;
    }

    public void setFormExceptions(Map<PartOfSpeech, List<Name>> formExceptions) {
        this.formExceptions = CollectionUtils.toImmutableMap(formExceptions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equal(id, word.id) &&
                Objects.equal(name, word.name) &&
                Objects.equal(transcriptions, word.transcriptions) &&
                Objects.equal(semanticBlocks, word.semanticBlocks) &&
                Objects.equal(formExceptions, word.formExceptions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, transcriptions, semanticBlocks, formExceptions);
    }
}
