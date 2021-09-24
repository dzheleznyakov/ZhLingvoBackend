package zh.lingvo.data.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zh.lingvo.core.LanguageDescriptorManager;

import java.io.IOException;

@Configuration
public class ServiceConfig {
    @Bean
    public LanguageDescriptorManager getLanguageDescriptorManager() throws IOException {
        return new LanguageDescriptorManager();
    }
}
