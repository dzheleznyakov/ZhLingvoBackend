package zh.lingvo.rest.converters.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import zh.lingvo.core.domain.PartOfSpeech;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class PosConversionRequest {
    private PartOfSpeech pos;
    private String langCode;
}
