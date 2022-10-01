package zh.lingvo.rest.converters;

import org.springframework.stereotype.Component;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.rest.commands.QuizRecordCommand;
import zh.lingvo.rest.commands.QuizRecordOverviewCommand;

@Component
public class QuizRecordConverter {
    private final QuizRecordToQuizRecordOverviewCommand recordToOverviewCommand;
    private final QuizRecordToQuizRecordCommand recordToCommand;
    private final QuizRecordCommandToQuizRecord commandToRecord;

    public QuizRecordConverter(
            QuizRecordToQuizRecordOverviewCommand recordToOverviewCommand,
            QuizRecordToQuizRecordCommand recordToCommand,
            QuizRecordCommandToQuizRecord commandToRecord
    ) {
        this.recordToOverviewCommand = recordToOverviewCommand;
        this.recordToCommand = recordToCommand;
        this.commandToRecord = commandToRecord;
    }

    public QuizRecordOverviewCommand toOverviewCommand(QuizRecord quizRecord) {
        return recordToOverviewCommand.convert(quizRecord);
    }

    public QuizRecordCommand toCommand(QuizRecord quizRecord) {
        return recordToCommand.convert(quizRecord);
    }

    public QuizRecord toQuizRecord(QuizRecordCommand command) {
        return commandToRecord.convert(command);
    }
}
