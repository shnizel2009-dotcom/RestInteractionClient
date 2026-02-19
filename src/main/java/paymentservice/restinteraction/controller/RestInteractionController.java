package paymentservice.restinteraction.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import paymentservice.restinteraction.model.dto.BankAccountCreateRequest;
import paymentservice.restinteraction.model.dto.BankAccountResponse;
import paymentservice.restinteraction.service.RestInteractionService;


@RestController
@RequestMapping("/api/v1/client-test")
@RequiredArgsConstructor
public class RestInteractionController {

    private final RestInteractionService restInteractionService;

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
}
