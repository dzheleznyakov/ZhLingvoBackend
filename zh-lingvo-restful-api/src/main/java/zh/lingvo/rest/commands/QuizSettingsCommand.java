package zh.lingvo.rest.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class QuizSettingsCommand {
    private Long quizId;
    private Integer maxScore;
    private String quizRegime;
    private String matchingRegime;
}
