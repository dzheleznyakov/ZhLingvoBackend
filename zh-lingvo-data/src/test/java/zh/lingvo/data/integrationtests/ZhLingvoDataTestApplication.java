package zh.lingvo.data.integrationtests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("zh.lingvo.data")
@EnableJpaRepositories("zh.lingvo.data.repositories")
@EntityScan("zh.lingvo.data.model")
public class ZhLingvoDataTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZhLingvoDataTestApplication.class, args);
    }
}
