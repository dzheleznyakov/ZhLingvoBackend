package zh.lingvo.rest.converters;

import org.springframework.stereotype.Component;
import zh.lingvo.data.model.Word;
import zh.lingvo.rest.commands.WordCommand;

@Component
public class WordConverter {
    private final WordToWordCommand wordToWordCommand;
    private final WordCommandToWord wordCommandToWord;

    public WordConverter(
            WordToWordCommand wordToWordCommand,
            WordCommandToWord wordCommandToWord
    ) {
        this.wordToWordCommand = wordToWordCommand;
        this.wordCommandToWord = wordCommandToWord;
    }

    public WordCommand toWordCommand(Word word) {
        return wordToWordCommand.convert(word);
    }

    public Word toWord(WordCommand wordCommand) {
        return wordCommandToWord.convert(wordCommand);
    }
}
