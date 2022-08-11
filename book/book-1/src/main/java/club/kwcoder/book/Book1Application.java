package club.kwcoder.book;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"club.kwcoder.book.repository"})
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableScheduling
public class Book1Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Book1Application.class);
        app.setBannerMode(Banner.Mode.LOG);
        app.run(args);
    }

}
