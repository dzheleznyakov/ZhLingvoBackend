package zh.lingvo.rest.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class QuizRecordCommand {
    private Long id;
    private String wordMainForm;
    private String pos;
    private String transcription;
    private Float currentScore;
    private Integer numberOfRuns;
    private Integer numberOfSuccesses;
    private List<TranslationCommand> translations;
    private List<ExampleCommand> examples;
}
