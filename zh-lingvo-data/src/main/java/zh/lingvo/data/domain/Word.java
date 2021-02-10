package zh.lingvo.data.domain;

import lombok.EqualsAndHashCode;
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
import javax.persistence.OrderBy;
import javax.persistence.PostPersist;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "dictionary")
@EqualsAndHashCode
@Entity(name = "word")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "dic_id", referencedColumnName = "id")
    private Dictionary dictionary;

    @Column(name = "main_form", nullable = false)
    private String mainForm;

    @Column(name = "transcription")
    private String transcription;

    @Column(name = "irreg_type")
    private String typeOfIrregularity;

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    @OrderBy("id")
    private List<SemanticBlock> semanticBlocks;
}
