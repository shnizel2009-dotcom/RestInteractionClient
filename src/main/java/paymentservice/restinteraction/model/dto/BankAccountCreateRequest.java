package paymentservice.restinteraction.model.dto;

import lombok.Data;

@Data
public class BankAccountCreateRequest {
    private Long customerId;
    private String number;
    private String currency;
    private String balance;

}
