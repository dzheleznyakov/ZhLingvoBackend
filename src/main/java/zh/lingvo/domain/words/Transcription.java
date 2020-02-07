package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Transcription {
    private String remark;
    private String ipa;

    public Transcription() {
    }

    public Transcription(String remark, String ipa) {
        this.remark = remark;
        this.ipa = ipa;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transcription that = (Transcription) o;
        return Objects.equal(remark, that.remark) &&
                Objects.equal(ipa, that.ipa);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(remark, ipa);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("remark", remark)
                .add("ipa", ipa)
                .toString();
    }
}
