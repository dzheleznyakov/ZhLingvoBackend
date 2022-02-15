package zh.lingvo.data.model.enums;

import java.util.Arrays;
import java.util.Objects;

public enum QuizRegime {
    FORWARD("f"),
    BACKWARD("b"),
    ALTERNATING("a");

    private final String code;

    QuizRegime(String  code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static QuizRegime fromCode(String code) {
        return Arrays.stream(QuizRegime.values())
                .filter(qr -> Objects.equals(qr.code, code))
                .findFirst()
                .orElse(null);
    }
}
