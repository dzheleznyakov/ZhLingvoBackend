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
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "meaning")
public class Meaning implements Persistable, SubWordPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "sem_bl_id", referencedColumnName = "id")
    private SemanticBlock semBlock;

    @Column(name = "remark")
    private String remark;

    @OneToMany(mappedBy = "meaning", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Translation> translations;

    @OneToMany(mappedBy = "meaning", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Example> examples;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meaning)) return false;
        Meaning meaning = (Meaning) o;
        return Objects.equal(id, meaning.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
