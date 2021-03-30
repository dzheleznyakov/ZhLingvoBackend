package zh.lingvo.data.model.converters;

import zh.lingvo.data.model.PartOfSpeech;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PartOfSpeechAttributeConverter implements AttributeConverter<PartOfSpeech, String> {
    @Override
    public String convertToDatabaseColumn(PartOfSpeech attribute) {
        return attribute.getCode();
    }

    @Override
    public PartOfSpeech convertToEntityAttribute(String dbData) {
        return PartOfSpeech.fromCode(dbData);
    }
}
