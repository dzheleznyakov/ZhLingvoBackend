package zh.lingvo.data.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "quiz")
public class Quiz {
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

    @OneToOne(optional = false)
    @JoinColumn(name = "settings_id", nullable = false, referencedColumnName = "id")
    private QuizSettings quizSettings;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("id")
    @ToString.Exclude
    private List<QuizRecord> quizRecords;

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
