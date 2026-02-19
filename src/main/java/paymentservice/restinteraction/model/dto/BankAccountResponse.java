package paymentservice.restinteraction.model.dto;

import lombok.Data;

@Data
public class BankAccountResponse {
    private Long bankAccountId;
    private Long customerId;
    private String accountNumber;
    private Double balance;
    private String currency;
}