package zh.lingvo.data.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "quiz_statistics")
public class QuizRecordStatistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "record_id", nullable = false)
    @ToString.Exclude
    private QuizRecord record;

    @Column(name = "current_score", nullable = false)
    private Float currentScore;

    @Column(name = "number_of_runs", nullable = false)
    private Integer numberOfRuns;

    @Column(name = "number_of_successes", nullable = false)
    private Integer numberOfSuccesses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizRecordStatistics)) return false;
        QuizRecordStatistics that = (QuizRecordStatistics) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
