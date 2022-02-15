package zh.lingvo.data.model.converters;

import zh.lingvo.data.model.enums.MatchingRegime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MatchingRegimeAttributeConverter implements AttributeConverter<MatchingRegime, String> {
    @Override
    public String convertToDatabaseColumn(MatchingRegime attribute) {
        return attribute.getCode();
    }

    @Override
    public MatchingRegime convertToEntityAttribute(String dbData) {
        return MatchingRegime.fromCode(dbData);
    }
}
