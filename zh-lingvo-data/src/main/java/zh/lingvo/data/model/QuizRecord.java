package zh.lingvo.data.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.converters.PartOfSpeechAttributeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "quiz_record")
public class QuizRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Quiz quiz;

    @Column(name = "word_main_form", nullable = false)
    private String wordMainForm;

    @Column(name="pos", nullable = false)
    @Convert(converter = PartOfSpeechAttributeConverter.class)
    private PartOfSpeech pos;

    @Column(name = "transcription")
    private String transcription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizRecord)) return false;
        QuizRecord that = (QuizRecord) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
