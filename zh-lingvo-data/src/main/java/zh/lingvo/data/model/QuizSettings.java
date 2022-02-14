package zh.lingvo.data.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zh.lingvo.data.model.converters.MatchingRegimeAttributeConverter;
import zh.lingvo.data.model.converters.QuizRegimeAttributeConverter;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "quiz_settings")
public class QuizSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Convert(converter = QuizRegimeAttributeConverter.class)
    @Column(name = "quiz_regime", nullable = false)
    private QuizRegime quizRegime;

    @Column(name = "max_score")
    private Integer maxScore;

    @Convert(converter = MatchingRegimeAttributeConverter.class)
    @Column(name = "matching_regime", nullable = false)
    private MatchingRegime matchingRegime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuizSettings)) return false;
        QuizSettings that = (QuizSettings) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
