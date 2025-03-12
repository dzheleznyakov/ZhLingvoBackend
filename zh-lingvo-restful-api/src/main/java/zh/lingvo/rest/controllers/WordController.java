package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zh.lingvo.data.exceptions.FailedToPersist;
import zh.lingvo.data.fixtures.PageableList;
import zh.lingvo.data.model.User;
import zh.lingvo.data.model.Word;
import zh.lingvo.data.services.WordService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.WordCommand;
import zh.lingvo.rest.converters.WordConverter;
import zh.lingvo.rest.exceptions.ResourceAlreadyExists;
import zh.lingvo.rest.exceptions.ResourceNotFound;
import zh.lingvo.rest.util.RequestContext;
import zh.lingvo.util.CollectionsHelper;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static zh.lingvo.util.Preconditions.checkNull;

@Slf4j
@ApiController
@RequestMapping("/api/words")
public class WordController {
    private final WordService wordService;
    private final WordConverter wordConverter;
    private final RequestContext context;

    public WordController(
            WordService wordService,
            WordConverter wordConverter,
            RequestContext context
    ) {
        this.wordService = wordService;
        this.wordConverter = wordConverter;
        this.context = context;
    }

    @GetMapping("/dictionary/{dictionaryId}")
    public WordListResponse getAllWords(
            @PathVariable("dictionaryId") Long dictionaryId,
            @RequestParam(name = "limit", defaultValue = "-1") Integer limit,
            @RequestParam(name = "offset", defaultValue = "-1") Integer offset
    ) {
        if (limit == null || offset == null || limit < 0 || offset < 0) {
            Set<String> words = wordService.findAll(dictionaryId, getUser())
                    .stream()
                    .map(Word::getMainForm)
                    .sorted()
                    .collect(CollectionsHelper.toLinkedHashSet());
            return new WordListResponse(words);
        }

        PageableList<Word> pageableWords = wordService.findAll(dictionaryId, getUser(), offset, limit);
        LinkedHashSet<String> words = pageableWords
                .stream()
                .map(Word::getMainForm)
                .sorted()
                .collect(CollectionsHelper.toLinkedHashSet());
        return new WordListResponse(
                words,
                pageableWords.getPageable().getPageSize(),
                pageableWords.getPageable().getOffset());
    }

    @GetMapping("/dictionary/{dictionaryId}/mainForm/{mainForm}")
    public List<WordCommand> getWordsByMainForm(
            @PathVariable("dictionaryId") Long dictionaryId,
            @PathVariable("mainForm") String mainForm
    ) {
        return wordService.findAllByMainForm(mainForm, dictionaryId, getUser())
                .stream()
                .map(wordConverter::toWordCommand)
                .sorted(Comparator.comparingLong(WordCommand::getId))
                .collect(ImmutableList.toImmutableList());
    }

    @GetMapping("/{wordId}")
    public WordCommand getWord(@PathVariable("wordId") long wordId) {
        return wordService.findWithSubWordPartsById(wordId, getUser())
                .map(wordConverter::toWordCommand)
                .orElseThrow(() -> new ResourceNotFound(String.format("Word [%d] not found", wordId)));
    }

    @PostMapping("/dictionary/{dictionaryId}")
    public WordCommand createWord(
            @PathVariable("dictionaryId") Long dictionaryId,
            @RequestBody WordCommand newWord
    ) {
        checkNull(newWord::getId,
                () -> new ResourceAlreadyExists(String.format("Word with id [%d] already exists", newWord.getId())));

        Word word = wordConverter.toWord(newWord);
        return wordService.create(word, dictionaryId, getUser())
                .map(wordConverter::toWordCommand)
                .orElseGet(WordCommand::new);
    }

    @PutMapping("/dictionary/{dictionaryId}/{wordId}")
    public WordCommand updateWord(
            @PathVariable("dictionaryId") Long dictionaryId,
            @PathVariable("wordId") Long wordId,
            @RequestBody WordCommand updatedWord
    ) {
        updatedWord.setId(wordId);
        Word word = wordConverter.toWord(updatedWord);
        try {
            Word savedWord = wordService.update(word, dictionaryId, getUser());
            return wordConverter.toWordCommand(savedWord);
        } catch (FailedToPersist e) {
            log.error("Error while updating word [{}]", wordId);
            throw new ResourceNotFound(e, String.format("Word [%d] not found", wordId));
        }
    }

    @DeleteMapping("/{wordId}")
    public Long deleteWord(@PathVariable("wordId") Long wordId) {
        Word word = Word.builder().id(wordId).build();
        wordService.delete(word, getUser());
        return wordId;
    }

    private User getUser() {
        return context.getUser();
    }

    @Getter
    public static class WordListResponse {
        private final Set<String> words;
        private final Map<String, Number> next;

        WordListResponse(Set<String> words) {
            this.words = words;
            next = null;
        }

        WordListResponse(Set<String> words, int limit, long offset) {
            this.words = words;
            this.next = ImmutableMap.of(
                    "limit", limit,
                    "offset", offset
            );
        }
    }
}
