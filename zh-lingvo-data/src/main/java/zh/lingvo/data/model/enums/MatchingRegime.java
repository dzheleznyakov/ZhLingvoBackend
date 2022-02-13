package zh.lingvo.data.model.enums;

import java.util.Arrays;
import java.util.Objects;

public enum MatchingRegime {
    STRICT("s"),
    LOOSENED("l"),
    RELAXED("r");

    private final String code;

    MatchingRegime(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static MatchingRegime fromCode(String code) {
        return Arrays.stream(MatchingRegime.values())
                .filter(mr -> Objects.equals(mr.code, code))
                .findFirst()
                .orElse(null);
    }
}
