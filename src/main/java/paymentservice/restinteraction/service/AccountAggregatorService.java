package paymentservice.restinteraction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import paymentservice.restinteraction.model.dto.BankAccountResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountAggregatorService {

    private final RestInteractionService restInteractionService;

    public LocalDateTime beforeCall;
    public LocalDateTime afterCall;

    public List<BankAccountResponse> getThreeAccounts(Long id1, Long id2, Long id3) {
        List<BankAccountResponse> accounts = new ArrayList<>();
        beforeCall = LocalDateTime.now();
        accounts.add(restInteractionService.getWithRestTemplate(id1));
        accounts.add(restInteractionService.getWithRestTemplate(id2));
        accounts.add(restInteractionService.getWithRestTemplate(id3));
        afterCall = LocalDateTime.now();
        return accounts;
    }

    public String getCallTime() {
        Duration duration = Duration.between(beforeCall, afterCall);
        return String.format(
                "Last call duration (sec): %d.%09d",
                duration.getSeconds(),
                duration.getNano()
        );

    }
}