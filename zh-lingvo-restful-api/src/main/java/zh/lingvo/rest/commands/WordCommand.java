package zh.lingvo.rest.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class WordCommand {
    private Long id;
    private String mainForm;
    private String transcription;
    private String typeOfIrregularity;
    private List<SemanticBlockCommand> semBlocks;
}
