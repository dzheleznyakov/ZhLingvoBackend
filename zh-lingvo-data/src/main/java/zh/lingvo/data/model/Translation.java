package zh.lingvo.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@ToString(exclude = "meaning")
@EqualsAndHashCode
@Entity(name = "translation")
public class Translation implements Persistable, SubWordPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "meaning_id", referencedColumnName = "id")
    private Meaning meaning;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "elaboration")
    private String elaboration;
}
