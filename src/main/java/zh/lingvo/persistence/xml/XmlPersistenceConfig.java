package zh.lingvo.persistence.xml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XmlPersistenceConfig {
    @Bean
    public XmlWordFactory getXmlWordFactory() {
        return new XmlWordFactory();
    }

    @Bean
    public XmlReader getXmlReader() {
        return new XmlReader();
    }

    @Bean
    public XmlWriter getXmlWriter() {
        return new XmlWriter();
    }
}
