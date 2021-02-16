package zh.lingvo.reactjsui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("zh.lingvo")
@EnableJpaRepositories("zh.lingvo.data")
@EntityScan("zh.lingvo.data.model")
public class ReactJsUiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactJsUiApplication.class, args);
    }
}
