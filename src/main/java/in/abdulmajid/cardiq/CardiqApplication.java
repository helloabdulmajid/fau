package in.abdulmajid.cardiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class CardiqApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardiqApplication.class, args);
        System.out.println("Hey Abdul, Apps Running...");
	}

}
