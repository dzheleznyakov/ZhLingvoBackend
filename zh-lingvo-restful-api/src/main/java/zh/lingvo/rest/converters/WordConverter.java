package zh.lingvo.rest.converters;

import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Word;
import zh.lingvo.rest.commands.WordCommand;
import zh.lingvo.rest.commands.WordOverviewCommand;

@Component
public class WordConverter {
    private final WordToWordOverviewCommand wordToWordOverviewCommand;
    private final WordToWordCommand wordToWordCommand;
    private final WordCommandToWord wordCommandToWord;

    public WordConverter(
            WordToWordOverviewCommand wordToWordOverviewCommand,
            WordToWordCommand wordToWordCommand,
            WordCommandToWord wordCommandToWord
    ) {
        this.wordToWordOverviewCommand = wordToWordOverviewCommand;
        this.wordToWordCommand = wordToWordCommand;
        this.wordCommandToWord = wordCommandToWord;
    }

    public WordOverviewCommand toWordOverviewCommand(Word word) {
        return wordToWordOverviewCommand.convert(word);
    }

    public WordCommand toWordCommand(Word word) {
        return wordToWordCommand.convert(word);
    }

    public Word toWord(WordCommand wordCommand) {
        return wordCommandToWord.convert(wordCommand);
    }
}
