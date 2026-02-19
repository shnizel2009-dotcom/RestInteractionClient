package paymentservice.restinteraction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import paymentservice.restinteraction.model.dto.BankAccountCreateRequest;
import paymentservice.restinteraction.model.dto.BankAccountResponse;

@FeignClient(name = "bank-account-client", url = "${app.api.base-url}")
public interface BankAccountFeignClient {

    @GetMapping("/accounts/{id}")
    BankAccountResponse getAccountById(@PathVariable("id") Long id);

    @PostMapping("/accounts")
    BankAccountResponse createAccount(@RequestBody BankAccountCreateRequest request);
}
