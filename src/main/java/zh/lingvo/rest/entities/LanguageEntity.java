package zh.lingvo.rest.entities;

import com.google.common.base.MoreObjects;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import zh.lingvo.util.ConfigReader;
import zh.lingvo.util.json.Persistable;

public class LanguageEntity implements JsonEntity {
    @Persistable
    private String code;

    @Persistable
    private String name;

    public LanguageEntity() {
    }

    public LanguageEntity(ConfigReader config) {
        this(config.getAsString("code"), config.getAsString("name"));
    }

    public LanguageEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("name", name)
                .toString();
    }
}
