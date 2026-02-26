package paymentservice.restinteraction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import paymentservice.restinteraction.client.BankAccountFeignClient;
import paymentservice.restinteraction.model.dto.BankAccountResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountAggregatorService {

    private final WebClient webClient;
    private final BankAccountFeignClient feignClient;

    public LocalDateTime beforeCall;
    public LocalDateTime afterCall;
    public Duration duration;
    public DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public List<BankAccountResponse> getThreeAccountsViaWebClient(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return List.of();
        }

        initBeforeCall();

        int concurrency = Math.min(10, idList.size());
        List<BankAccountResponse> result = Flux.fromIterable(idList)
                .flatMap(id -> webClient.get()
                        .uri("/accounts/{id}", id)
                        .retrieve()
                        .bodyToMono(BankAccountResponse.class),
                        concurrency)
                .collectList()
                .block(Duration.ofSeconds(5));

        initAfterCall();

        return result;
    }

    @Cacheable(cacheNames = "accounts", key = "#idList")
    public List<BankAccountResponse> getThreeAccountsViaFeign(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return List.of();
        }

//        initBeforeCall();

        List<CompletableFuture<BankAccountResponse>> futures = idList.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> feignClient.getAccountById(id))
                        .orTimeout(5, TimeUnit.SECONDS))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<BankAccountResponse> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

//        initAfterCall();
        log.info("CACHE MISS -> calling payment-service via Feign, ids={}", idList);

        return result;
    }

    public String getCallTime() {
        initDuration();
        return String.format(
                "Last call duration (sec): %d.%09d",
                duration.getSeconds(),
                duration.getNano()
        );

    }

    private void initDuration() {
        duration = Duration.between(beforeCall, afterCall);
    }

    private void initBeforeCall() {
        beforeCall = LocalDateTime.now();
        log.info("Fix before call: {}", LocalDateTime.now().format(fmt));
    }

    private void initAfterCall() {
        afterCall = LocalDateTime.now();
        log.info("Fix after call: {}", LocalDateTime.now().format(fmt));
        initDuration();
        log.info(String.format(
                "Last call duration (sec): %d.%09d",
                duration.getSeconds(),
                duration.getNano()));
    }
}
