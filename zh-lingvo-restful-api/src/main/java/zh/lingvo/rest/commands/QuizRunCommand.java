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
public class QuizRunCommand {
    private Long id;
    private Long quizId;
    private String quizRegime;
    private String matchingRegime;
    private Long ts;
    private List<Long> records;
    private List<DoneRecord> doneRecords;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DoneRecord {
        private Long recordId;
        private Boolean correct;
    }
}
