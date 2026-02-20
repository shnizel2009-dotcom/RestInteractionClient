package paymentservice.restinteraction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import paymentservice.restinteraction.model.dto.BankAccountResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountAggregatorService {

    private final RestInteractionService restInteractionService;

    public List<BankAccountResponse> getThreeAccounts(Long id1, Long id2, Long id3) {
        List<BankAccountResponse> accounts = new ArrayList<>();

        accounts.add(restInteractionService.getWithRestTemplate(id1));
        accounts.add(restInteractionService.getWithRestTemplate(id2));
        accounts.add(restInteractionService.getWithRestTemplate(id3));

        return accounts;
    }
}