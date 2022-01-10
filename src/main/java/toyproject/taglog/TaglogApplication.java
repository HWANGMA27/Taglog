package toyproject.taglog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TaglogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaglogApplication.class, args);
	}

}
