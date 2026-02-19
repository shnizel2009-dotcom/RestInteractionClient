package paymentservice.restinteraction.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class RestInteractionConfig {

    @Value("${app.api.base-url}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.create(baseUrl);
    }
}
