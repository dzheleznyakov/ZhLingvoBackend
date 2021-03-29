package zh.lingvo.rest.converters;

import org.springframework.core.convert.ConversionException;

public class ZhLingvoConversionException extends ConversionException {
    public ZhLingvoConversionException(String pattern, Object... params) {
        super(String.format(pattern, params));
    }
}
