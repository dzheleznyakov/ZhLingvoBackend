package zh.lingvo.data.model;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zh.lingvo.data.fixtures.Persistable;
import zh.lingvo.data.model.converters.MatchingRegimeAttributeConverter;
import zh.lingvo.data.model.converters.QuizRegimeAttributeConverter;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.data.model.enums.QuizRegime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "quiz")
public class Quiz implements Persistable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @ToString.Exclude
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_lang_id", nullable = false, referencedColumnName = "id")
    private Language language;

    @Column(name = "name")
    private String name;

    @Convert(converter = QuizRegimeAttributeConverter.class)
    @Column(name = "quiz_regime", nullable = false)
    @Builder.Default
    private QuizRegime quizRegime = QuizRegime.FORWARD;

    @Column(name = "max_score")
    @Builder.Default
    private Integer maxScore = 30;

    @Convert(converter = MatchingRegimeAttributeConverter.class)
    @Column(name = "matching_regime", nullable = false)
    @Builder.Default
    private MatchingRegime matchingRegime = MatchingRegime.LOOSENED;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id")
    @ToString.Exclude
    private List<QuizRecord> quizRecords;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("accessed_timestamp")
    @Builder.Default
    @ToString.Exclude
    private List<QuizRun> quizRuns = ImmutableList.of();

    public static void merge(Quiz baseQuiz, Quiz otherQuiz) {
        if (otherQuiz.getName() != null)
            baseQuiz.setName(otherQuiz.getName());
        if (otherQuiz.getLanguage() != null)
            baseQuiz.setLanguage(otherQuiz.getLanguage());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quiz)) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equal(id, quiz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
