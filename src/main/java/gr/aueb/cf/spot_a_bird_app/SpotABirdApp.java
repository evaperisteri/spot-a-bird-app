package gr.aueb.cf.spot_a_bird_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpotABirdApp {

	public static void main(String[] args) {
		SpringApplication.run(SpotABirdApp.class, args);
	}

}
