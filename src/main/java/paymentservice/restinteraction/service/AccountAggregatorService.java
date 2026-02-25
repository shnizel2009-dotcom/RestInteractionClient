package paymentservice.restinteraction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import paymentservice.restinteraction.model.dto.BankAccountResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountAggregatorService {

    private final WebClient webClient;

    public LocalDateTime beforeCall;
    public LocalDateTime afterCall;
    public Duration duration;
    public DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public List<BankAccountResponse> getThreeAccounts(List<Long> idList) {
        if (idList == null || idList.isEmpty()) {
            return List.of();
        }

        beforeCall = LocalDateTime.now();
        log.info("Fix before call: {}", LocalDateTime.now().format(fmt));

        int concurrency = Math.min(10, idList.size());
        List<BankAccountResponse> result = Flux.fromIterable(idList)
                .flatMap(id -> webClient.get()
                        .uri("/accounts/{id}", id)
                        .retrieve()
                        .bodyToMono(BankAccountResponse.class),
                        concurrency)
                .collectList()
                .block(Duration.ofSeconds(5));

        afterCall = LocalDateTime.now();
        log.info("Fix after call: {}", LocalDateTime.now().format(fmt));
        initDuration();
        log.info(String.format(
                "Last call duration (sec): %d.%09d",
                duration.getSeconds(),
                duration.getNano()));

        return result;
    }

//    public List<BankAccountResponse> getThreeAccounts(List<Long> idList) {
//
//        List<BankAccountResponse> accounts = new ArrayList<>();
//
//        beforeCall = LocalDateTime.now();
//        log.info("Fix before call: {}", LocalDateTime.now().format(fmt));
//
//        for(Long id : idList) {
//            accounts.add((restInteractionService.getWithRestTemplate(id)));
//        }
//        log.info("Fix after call: {}", LocalDateTime.now().format(fmt));
//        afterCall = LocalDateTime.now();
//        initDuration();
//        log.info(String.format(
//                "Last call duration (sec): %d.%09d",
//                duration.getSeconds(),
//                duration.getNano()));
//
//        return accounts;
//    }

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
}
