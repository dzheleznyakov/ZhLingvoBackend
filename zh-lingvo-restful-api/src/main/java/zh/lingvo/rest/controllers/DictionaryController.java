package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.Dictionary;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.DictionaryService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.DictionaryCommand;
import zh.lingvo.rest.converters.DictionaryCommandToDictionary;
import zh.lingvo.rest.converters.DictionaryToDictionaryCommand;
import zh.lingvo.rest.exceptions.RequestMalformed;
import zh.lingvo.rest.exceptions.ResourceNotFound;
import zh.lingvo.rest.util.RequestContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static zh.lingvo.util.Preconditions.checkNull;

@ApiController
@RequestMapping("/api/dictionaries")
public class DictionaryController {
    private final DictionaryService dictionaryService;
    private final DictionaryToDictionaryCommand dictionaryConverter;
    private final DictionaryCommandToDictionary dictionaryCommandConverter;
    private final RequestContext requestContext;

    public DictionaryController(
            DictionaryService dictionaryService,
            DictionaryToDictionaryCommand dictionaryConverter,
            DictionaryCommandToDictionary dictionaryCommandConverter,
            RequestContext requestContext
    ) {
        this.dictionaryService = dictionaryService;
        this.dictionaryConverter = dictionaryConverter;
        this.dictionaryCommandConverter = dictionaryCommandConverter;
        this.requestContext = requestContext;
    }

    @GetMapping
    public List<DictionaryCommand> getAllDictionaries() {
        return dictionaryService
                .findAllByUser(getUser())
                .stream()
                .map(dictionaryConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }

    @GetMapping("/{id}")
    public DictionaryCommand getDictionary(@PathVariable("id") long id) {
        return dictionaryService.findById(id)
                .filter(dictionary -> Objects.equals(requestContext.getUser(), dictionary.getUser()))
                .map(dictionaryConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format("Dictionary id=[%d] not found", id)));
    }

    @PostMapping
    public DictionaryCommand createNewDictionary(@RequestBody DictionaryCommand command) {
        checkNull(command::getId,
                () -> new RequestMalformed(String.format("Dictionary create request should not contain id; found [%d]", command.getId())));
        Dictionary toSave = dictionaryCommandConverter.convert(command);
        Dictionary saved = dictionaryService.save(toSave);
        return dictionaryConverter.convert(saved);
    }

    @PutMapping
    public DictionaryCommand updateDictionary(@RequestBody DictionaryCommand command) {
        Optional<Dictionary> byId = dictionaryService.findById(command.getId());
        Dictionary toSave = byId
                .filter(dictionary -> Objects.equals(requestContext.getUser(), dictionary.getUser()))
                .orElseThrow(() -> new ResourceNotFound(String.format("Dictionary id=[%d] not found", command.getId())));
        Dictionary saved = dictionaryService.save(toSave);
        return dictionaryConverter.convert(saved);
    }

    private User getUser() {
        return requestContext.getUser();
    }
}
