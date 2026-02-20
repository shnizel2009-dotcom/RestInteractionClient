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

    public BankAccountResponse getWithRestTemplate(Long id) {
        URI url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/accounts/{id}")
                .build(id);
        return restTemplate.getForObject(url, BankAccountResponse.class);
    }

    public BankAccountResponse getWithRestClient(Long id) {
        return restClient.get()
                .uri(UriComponentsBuilder
                        .fromHttpUrl(baseUrl)
                        .path("/accounts/{id}")
                        .build(id))
                .retrieve()
                .body(BankAccountResponse.class);
    }

    public BankAccountResponse getWithFeign(Long id) {
        return feignClient.getAccountById(id);
    }

    public BankAccountResponse getWithWebClient(Long id) {
        return webClient.get()
                .uri(UriComponentsBuilder
                        .fromHttpUrl(baseUrl)
                        .path("/accounts/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(BankAccountResponse.class)
                .block();
    }

    public BankAccountResponse postWithRestTemplate(BankAccountCreateRequest request) {
        URI url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/accounts")
                .build()
                .toUri();
        return restTemplate.postForObject(url, request, BankAccountResponse.class);
    }

    public BankAccountResponse postWithRestClient(BankAccountCreateRequest request) {
        return restClient.post()
                .uri(UriComponentsBuilder
                        .fromHttpUrl(baseUrl)
                        .path("/accounts")
                        .build()
                        .toUri())
                .body(request)
                .retrieve()
                .body(BankAccountResponse.class);
    }

    public BankAccountResponse postWithFeign(BankAccountCreateRequest request) {
        return feignClient.createAccount(request);
    }

    public BankAccountResponse postWithWebClient(BankAccountCreateRequest request) {
        return webClient.post()
                .uri(UriComponentsBuilder
                        .fromHttpUrl(baseUrl)
                        .path("/accounts")
                        .build()
                        .toUri())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BankAccountResponse.class)
                .block();
    }

    public BankAccountResponse getSomeAccounts(BankAccountCreateRequest request) {
        URI url = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/accounts")
                .build()
                .toUri();
        return restTemplate.postForObject(url, request, BankAccountResponse.class);
    }
}
