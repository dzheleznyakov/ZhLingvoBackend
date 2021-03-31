package zh.lingvo.rest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.util.RequestContext;

@ApiController
@RequestMapping("/api/words")
public class WordController {
    private final RequestContext context;

    public WordController(RequestContext context) {
        this.context = context;
    }
}
