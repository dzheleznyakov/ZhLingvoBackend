package zh.lingvo.rest.entities;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.Language;

public class LanguageRestEntity implements JsonEntity {
    private String code;

    private String name;

    public LanguageRestEntity() {
    }

    public LanguageRestEntity(Language language) {
        this.code = language.getCode();
        this.name = language.getName();
    }

    public LanguageRestEntity(String code, String name) {
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
