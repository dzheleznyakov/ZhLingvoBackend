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

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "words")
@EqualsAndHashCode
@Entity(name = "dictionary")
public class Dictionary implements Persistable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lang_id", referencedColumnName = "id")
    private Language language;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "dictionary", fetch = FetchType.LAZY)
    @OrderBy("main_form")
    private List<Word> words;
}
