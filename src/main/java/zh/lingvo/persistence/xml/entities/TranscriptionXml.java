package zh.lingvo.persistence.xml.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

@JacksonXmlRootElement(localName = "transcription")
public class TranscriptionXml implements XmlEntity {
    @JacksonXmlProperty(isAttribute = true)
    private String remark;

    @JacksonXmlProperty(isAttribute = true)
    private String ipa;

    public TranscriptionXml() {
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
        TranscriptionXml that = (TranscriptionXml) o;
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
                .omitNullValues()
                .add("remark", remark)
                .add("ipa", ipa)
                .toString();
    }
}
