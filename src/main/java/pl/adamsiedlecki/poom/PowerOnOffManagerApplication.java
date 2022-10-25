package pl.adamsiedlecki.poom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PowerOnOffManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PowerOnOffManagerApplication.class, args);
	}

}
