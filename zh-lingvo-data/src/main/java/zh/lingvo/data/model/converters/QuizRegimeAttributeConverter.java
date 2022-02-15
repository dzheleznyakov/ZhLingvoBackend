package zh.lingvo.data.model.converters;

import zh.lingvo.data.model.enums.QuizRegime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class QuizRegimeAttributeConverter implements AttributeConverter<QuizRegime, String> {
    @Override
    public String convertToDatabaseColumn(QuizRegime attribute) {
        return attribute.getCode();
    }

    @Override
    public QuizRegime convertToEntityAttribute(String dbData) {
        return QuizRegime.fromCode(dbData);
    }
}
