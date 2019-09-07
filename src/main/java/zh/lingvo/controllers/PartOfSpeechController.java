package zh.lingvo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zh.lingvo.ApiMapping;
import zh.lingvo.domain.PartOfSpeech;

import java.util.List;
import java.util.Map;

@RestController
@ApiMapping
@RequestMapping("/api/partsOfSpeeches")
public class PartOfSpeechController {
    @GetMapping("/{lang}")
    public List<String> getPartOfSpeeches(@PathVariable("lang") String languageCode) {
        return PartOfSpeech.getPosNames(languageCode);
    }
}
