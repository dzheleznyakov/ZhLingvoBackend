package zh.lingvo.data.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zh.lingvo.data.fixtures.Persistable;
import zh.lingvo.data.fixtures.SubWordPart;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "translation")
public class Translation implements Persistable, SubWordPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "meaning_id", referencedColumnName = "id")
    @ToString.Exclude
    private Meaning meaning;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "elaboration")
    private String elaboration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Translation)) return false;
        Translation that = (Translation) o;
        return Objects.equal(id, that.id) && Objects.equal(value, that.value) && Objects.equal(elaboration, that.elaboration);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, value, elaboration);
    }
}
