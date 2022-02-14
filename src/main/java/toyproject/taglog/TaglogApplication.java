package toyproject.taglog;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;

@EnableJpaAuditing
@SpringBootApplication
public class TaglogApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaglogApplication.class, args);
    }

    @Bean
    JPAQueryFactory createJpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }
}
