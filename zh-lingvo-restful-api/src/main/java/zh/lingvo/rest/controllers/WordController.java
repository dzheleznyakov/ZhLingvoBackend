package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.WordService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.WordCommand;
import zh.lingvo.rest.commands.WordOverviewCommand;
import zh.lingvo.rest.converters.WordToWordCommand;
import zh.lingvo.rest.converters.WordToWordOverviewCommand;
import zh.lingvo.rest.exceptions.ResourceNotFound;
import zh.lingvo.rest.util.RequestContext;

import java.util.List;

@ApiController
@RequestMapping("/api/words")
public class WordController {
    private final WordService wordService;
    private final WordToWordOverviewCommand wordOverviewConverter;
    private final WordToWordCommand wordConverter;
    private final RequestContext context;

    public WordController(WordService wordService, WordToWordOverviewCommand wordOverviewConverter, WordToWordCommand wordConverter, RequestContext context) {
        this.wordService = wordService;
        this.wordOverviewConverter = wordOverviewConverter;
        this.wordConverter = wordConverter;
        this.context = context;
    }

    @GetMapping("/dictionary/{dictionaryId}")
    public List<WordOverviewCommand> getAllWords(@PathVariable("dictionaryId") long dictionaryId) {
        return wordService.findAll(dictionaryId, getUser())
                .stream()
                .map(wordOverviewConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }

    @GetMapping("/{wordId}")
    public WordCommand getWord(@PathVariable("wordId") long wordId) {
        return wordService.findWithSubWordPartsById(wordId, getUser())
                .map(wordConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format("Word [%d] not found", wordId)));
    }

    private User getUser() {
        return context.getUser();
    }
}
