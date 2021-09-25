package zh.lingvo.data.configs;

import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zh.lingvo.core.LanguageDescriptor;
import zh.lingvo.core.LanguageDescriptorManager;
import zh.lingvo.core.descriptors.EnglishLanguageDescriptor;
import zh.lingvo.core.descriptors.RussianLanguageDescriptor;
import zh.lingvo.core.descriptors.SpanishLanguageDescriptor;

import java.io.IOException;
import java.util.Collection;

@Configuration
public class ServiceConfig {
    @Bean
    public LanguageDescriptorManager getLanguageDescriptorManager() throws IOException {
        return new LanguageDescriptorManager(getLanguageDescriptors());
    }

    private Collection<LanguageDescriptor> getLanguageDescriptors() {
        return ImmutableList.of(
                new EnglishLanguageDescriptor(),
                new SpanishLanguageDescriptor(),
                new RussianLanguageDescriptor()
        );
    }
}
