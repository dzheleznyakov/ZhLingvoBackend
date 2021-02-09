package zh.lingvo.data.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
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
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "semBlock")
@EqualsAndHashCode
@Entity(name = "meaning")
public class Meaning implements Persistable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "sem_bl_id", referencedColumnName = "id")
    private SemanticBlock semBlock;

    @Column(name = "remark")
    private String remark;

    @OneToMany(mappedBy = "meaning", fetch = FetchType.EAGER)
    private Set<Translation> translations;

    @OneToMany(mappedBy = "meaning", fetch = FetchType.EAGER)
    private Set<Example> examples;
}
