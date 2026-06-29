package in.abdulmajid.fau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class FauApplication {

	public static void main(String[] args) {
		SpringApplication.run(FauApplication.class, args);
        System.out.println("Hey Abdul, Apps Running...");
	}

}
