package zh.lingvo.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zh.lingvo.core.PartOfSpeech;
import zh.lingvo.data.fixtures.Persistable;
import zh.lingvo.data.fixtures.SubWordPart;
import zh.lingvo.data.model.converters.PartOfSpeechAttributeConverter;

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
@ToString(exclude = "word")
@EqualsAndHashCode(of = "id")
@Entity(name = "semantic_block")
public class SemanticBlock implements Persistable, SubWordPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "word_id", referencedColumnName = "id")
    private Word word;

    @Column(name = "pos", nullable = false)
    @Convert(converter = PartOfSpeechAttributeConverter.class)
    private PartOfSpeech pos;

    @Column(name = "gender")
    private String gender;

    @OneToMany(mappedBy = "semBlock", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    private List<Meaning> meanings;
}
