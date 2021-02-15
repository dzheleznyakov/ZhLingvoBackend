package zh.lingvo.data.repositories;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:/test.properties")
@EnableJpaRepositories(basePackages = "zh.lingvo.data.repositories")
@EntityScan("zh.lingvo.data.model")
public abstract class BaseRepositoryTest<T extends CrudRepository<?, ?>> {
    @Autowired
    protected TestEntityManager entityManager;

    @Autowired
    protected T repository;
}
