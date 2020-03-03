package zh.lingvo.rest.entities;

import zh.lingvo.domain.Gender;
import zh.lingvo.domain.PartOfSpeech;

import java.util.Map;

public class LanguageConstants implements JsonEntity {
    private Map<PartOfSpeech, String> pos;
    private Map<Gender, String> genders;

    public LanguageConstants() {
    }

    public Map<PartOfSpeech, String> getPos() {
        return pos;
    }

    public void setPos(Map<PartOfSpeech, String> pos) {
        this.pos = pos;
    }

    public Map<Gender, String> getGenders() {
        return genders;
    }

    public void setGenders(Map<Gender, String> genders) {
        this.genders = genders;
    }
}
