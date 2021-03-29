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
public class MeaningCommand {
    private Long id;
    private String remark;
    private List<TranslationCommand> translations;
    private List<ExampleCommand> examples;
}
