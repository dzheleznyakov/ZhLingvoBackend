package zh.lingvo.rest.controllers;

import com.google.common.collect.ImmutableList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.services.PosService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.PosCommand;
import zh.lingvo.rest.converters.PosToPosCommand;
import zh.lingvo.rest.converters.requests.PosConversionRequest;

import java.util.List;

@ApiController
@RequestMapping("/api/pos")
public class PosController {
    private final PosService posService;
    private final PosToPosCommand posConverter;

    public PosController(PosService posService, PosToPosCommand posConverter) {
        this.posService = posService;
        this.posConverter = posConverter;
    }

    @GetMapping("/{langCode}")
    public List<PosCommand> getAllPos(@PathVariable("langCode") String langCode) {
        return posService.findAll(langCode).stream()
                .map(pos -> PosConversionRequest.builder()
                                .langCode(langCode)
                                .pos(pos)
                                .build())
                .map(posConverter::convert)
                .collect(ImmutableList.toImmutableList());
    }

}
