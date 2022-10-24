package zh.lingvo.data.model.enums;

import com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
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

    public static final List<String> STRING_LIST = Arrays.stream(QuizRegime.values())
            .map(QuizRegime::toString)
            .collect(ImmutableList.toImmutableList());
}
