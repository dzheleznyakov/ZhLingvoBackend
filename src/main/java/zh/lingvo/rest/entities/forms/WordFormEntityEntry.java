package zh.lingvo.rest.entities.forms;

import zh.lingvo.domain.linguisticcategories.LinguisticCategory;
import zh.lingvo.rest.entities.JsonEntity;

public class WordFormEntityEntry implements JsonEntity {
    private LinguisticCategory[] projection;
    private String form;

    public WordFormEntityEntry() {
    }

    public WordFormEntityEntry(LinguisticCategory[] projection, String form) {
        this.projection = projection;
        this.form = form;
    }

    public LinguisticCategory[] getProjection() {
        return projection;
    }

    public void setProjection(LinguisticCategory[] projection) {
        this.projection = projection;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }
}
