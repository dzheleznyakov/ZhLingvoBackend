package zh.lingvo.data.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zh.lingvo.data.fixtures.Persistable;
import zh.lingvo.data.model.converters.LongListAttributeConverter;
import zh.lingvo.data.model.converters.LongToBooleanMapAttributeConverter;
import zh.lingvo.data.model.converters.MatchingRegimeAttributeConverter;
import zh.lingvo.data.model.converters.QuizRegimeAttributeConverter;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "quiz_run")
public class QuizRun implements Persistable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    @ToString.Exclude
    private Quiz quiz;

    @Convert(converter = QuizRegimeAttributeConverter.class)
    @Column(name = "quiz_regime", nullable = false)
    private QuizRegime quizRegime;

    @Convert(converter = MatchingRegimeAttributeConverter.class)
    @Column(name = "matching_regime", nullable = false)
    private MatchingRegime matchingRegime;

    @Convert(converter = LongListAttributeConverter.class)
    @Column(name = "records")
    private List<Long> records;

    @Convert(converter = LongToBooleanMapAttributeConverter.class)
    @Column(name = "done_records")
    private Map<Long, Boolean> doneRecords;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizRun)) return false;
        QuizRun quizRun = (QuizRun) o;
        return Objects.equal(id, quizRun.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
