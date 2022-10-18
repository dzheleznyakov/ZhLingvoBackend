package zh.lingvo.rest.converters;

import org.springframework.stereotype.Component;
import zh.lingvo.core.domain.PartOfSpeech;
import zh.lingvo.data.model.QuizRecord;
import zh.lingvo.rest.commands.QuizRecordCommand;
import zh.lingvo.rest.commands.QuizRecordOverviewCommand;

import java.util.function.Consumer;
import java.util.function.Supplier;

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

    public QuizRecord createQuizRecord(QuizRecordCommand command) {
        QuizRecord quizRecord = new QuizRecord();
        setField(quizRecord::setId, command::getId, quizRecord::getId);
        setField(quizRecord::setWordMainForm, command::getWordMainForm, quizRecord::getWordMainForm);
        setField(quizRecord::setPos, () -> PartOfSpeech.fromShortName(command.getPos()), quizRecord::getPos);
        setField(quizRecord::setTranscription, command::getTranscription, quizRecord::getTranscription);
        setField(quizRecord::setNumberOfRuns, command::getNumberOfRuns, () -> 0);
        setField(quizRecord::setNumberOfSuccesses, command::getNumberOfSuccesses, () -> 0);
        quizRecord.setCurrentScore(0f);
        return quizRecord;
    }

    private <FieldType> void setField(Consumer<FieldType> setter, Supplier<FieldType> main, Supplier<FieldType> fallback) {
        FieldType value = main.get();
        if (value == null)
            value = fallback.get();
        setter.accept(value);
    }
}
