package paymentservice.restinteraction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RestInteractionApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestInteractionApplication.class, args);
	}
}
