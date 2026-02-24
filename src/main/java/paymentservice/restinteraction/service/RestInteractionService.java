package paymentservice.restinteraction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import paymentservice.restinteraction.client.BankAccountFeignClient;
import paymentservice.restinteraction.model.dto.BankAccountCreateRequest;
import paymentservice.restinteraction.model.dto.BankAccountResponse;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestInteractionService {

    @Value("${app.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;
    private final RestClient restClient;
    private final BankAccountFeignClient feignClient;
    private final WebClient webClient;

    private URI createUri(String path, Object... pathVars) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path(path);

        return (pathVars == null || pathVars.length == 0)
                ? builder.build().toUri()
                : builder.buildAndExpand(pathVars).toUri();
    }

    public BankAccountResponse getWithRestTemplate(Long id) {
        URI url = createUri("/accounts/{id}", id);
        return restTemplate.getForObject(url, BankAccountResponse.class);
    }

    public BankAccountResponse getWithRestClient(Long id) {
        return restClient.get()
                .uri(createUri("/accounts/{id}", id))
                .retrieve()
                .body(BankAccountResponse.class);
    }

    public BankAccountResponse getWithFeign(Long id) {
        return feignClient.getAccountById(id);
    }

    public BankAccountResponse getWithWebClient(Long id) {
        return webClient.get()
                .uri(createUri("/accounts/{id}", id))
                .retrieve()
                .bodyToMono(BankAccountResponse.class)
                .block();
    }

    public BankAccountResponse postWithRestTemplate(BankAccountCreateRequest request) {
        URI url = createUri("/accounts");
        return restTemplate.postForObject(url, request, BankAccountResponse.class);
    }

    public BankAccountResponse postWithRestClient(BankAccountCreateRequest request) {
        return restClient.post()
                .uri(createUri("/accounts"))
                .body(request)
                .retrieve()
                .body(BankAccountResponse.class);
    }

    public BankAccountResponse postWithFeign(BankAccountCreateRequest request) {
        return feignClient.createAccount(request);
    }

    public BankAccountResponse postWithWebClient(BankAccountCreateRequest request) {
        return webClient.post()
                .uri(createUri("/accounts"))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BankAccountResponse.class)
                .block();
    }
}
