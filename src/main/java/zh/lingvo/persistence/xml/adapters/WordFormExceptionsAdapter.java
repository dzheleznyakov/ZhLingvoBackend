package zh.lingvo.persistence.xml.adapters;

import zh.lingvo.domain.PartOfSpeech;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Map;

public class WordFormExceptionsAdapter extends XmlAdapter<WordFormExceptionsAdapter.ValueType, Map<PartOfSpeech, Map<Enum<?>, String>>> {

    @Override
    public Map<PartOfSpeech, Map<Enum<?>, String>> unmarshal(ValueType v) throws Exception {
        return null;
    }

    @Override
    public ValueType marshal(Map<PartOfSpeech, Map<Enum<?>, String>> v) throws Exception {
        return null;
    }

    static class ValueType {

    }
}
