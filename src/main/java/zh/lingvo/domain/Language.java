package zh.lingvo.domain;

import zh.lingvo.util.ConfigReader;

public class Language {
    private final String code;
    private final String name;

    public Language(ConfigReader config) {
        this.code = config.getString("code");
        this.name = config.getString("name");
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
