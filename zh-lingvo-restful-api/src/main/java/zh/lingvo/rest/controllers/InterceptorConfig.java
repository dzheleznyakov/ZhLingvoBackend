package zh.lingvo.rest.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    private final AuthorisationInterceptor authorisationInterceptor;

    public InterceptorConfig(AuthorisationInterceptor authorisationInterceptor) {
        this.authorisationInterceptor = authorisationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorisationInterceptor)
                .addPathPatterns("/api/*");
    }
}
