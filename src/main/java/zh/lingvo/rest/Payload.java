package zh.lingvo.rest;

import com.google.common.base.MoreObjects;

public class Payload {
    private String data;

    public Payload() {
    }

    public String getData() {
        return data == null ? data : data.trim();
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("data", data)
                .toString();
    }
}
