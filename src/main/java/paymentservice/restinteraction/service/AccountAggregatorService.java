package paymentservice.restinteraction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import paymentservice.restinteraction.model.dto.BankAccountResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountAggregatorService {

    private final RestInteractionService restInteractionService;

    public LocalDateTime beforeCall;
    public LocalDateTime afterCall;
    public Duration duration;
    public DateTimeFormatter fmt =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public List<BankAccountResponse> getThreeAccounts(List<Long> idList) {

        List<BankAccountResponse> accounts = new ArrayList<>();

        beforeCall = LocalDateTime.now();
        log.info("Fix before call: {}", LocalDateTime.now().format(fmt));

        for(Long id : idList) {
            accounts.add((restInteractionService.getWithRestTemplate(id)));
        }
        log.info("Fix after call: {}", LocalDateTime.now().format(fmt));
        afterCall = LocalDateTime.now();
        initDuration();
        log.info(String.format(
                "Last call duration (sec): %d.%09d",
                duration.getSeconds(),
                duration.getNano()));

        return accounts;
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
}