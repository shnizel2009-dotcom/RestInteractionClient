package paymentservice.restinteraction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import paymentservice.restinteraction.model.dto.BankAccountCreateRequest;
import paymentservice.restinteraction.model.dto.BankAccountResponse;
import paymentservice.restinteraction.service.AccountAggregatorService;
import paymentservice.restinteraction.service.RestInteractionService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/client-test")
@RequiredArgsConstructor
public class RestInteractionController {

    private final RestInteractionService restInteractionService;
    private final AccountAggregatorService accountAggregatorService;

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    @GetMapping("/rest-template/{id}")
    public BankAccountResponse testRestTemplate(@PathVariable("id") Long id) {
        return restInteractionService.getWithRestTemplate(id);
    }

    @GetMapping("/rest-client/{id}")
    public BankAccountResponse testRestClient(@PathVariable("id") Long id) {
        return restInteractionService.getWithRestClient(id);
    }

    @GetMapping("/feign/{id}")
    public BankAccountResponse testFeign(@PathVariable("id") Long id) {
        return restInteractionService.getWithFeign(id);
    }

    @GetMapping("/web-client/{id}")
    public BankAccountResponse testWebClient(@PathVariable("id") Long id) {
        return restInteractionService.getWithWebClient(id);
    }

    @PostMapping("/rest-template")
    public BankAccountResponse testRestTemplatePost(@RequestBody BankAccountCreateRequest request) {
        return restInteractionService.postWithRestTemplate(request);
    }

    @PostMapping("/rest-client")
    public BankAccountResponse testRestClientPost(@RequestBody BankAccountCreateRequest request) {
        return restInteractionService.postWithRestClient(request);
    }

    @PostMapping("/feign")
    public BankAccountResponse testFeignPost(@RequestBody BankAccountCreateRequest request) {
        return restInteractionService.postWithFeign(request);
    }

    @PostMapping("/web-client")
    public BankAccountResponse testWebClientPost(@RequestBody BankAccountCreateRequest request) {
        return restInteractionService.postWithWebClient(request);
    }

    @GetMapping("/aggregator")
    public List<BankAccountResponse> testAggregator(
            @RequestParam("id1") Long id1,
            @RequestParam("id2") Long id2,
            @RequestParam("id3") Long id3) {
        return accountAggregatorService.getThreeAccounts(id1, id2, id3);
    }

    @GetMapping("/get-call-time")
    public String testGetCallTime() {
        return accountAggregatorService.getCallTime();
    }
}
