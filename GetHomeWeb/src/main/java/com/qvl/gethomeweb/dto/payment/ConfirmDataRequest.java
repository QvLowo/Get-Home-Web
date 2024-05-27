package com.qvl.gethomeweb.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ConfirmDataRequest {
    private String paymentId;
    private String transactionId;
    private BigDecimal amount;
    private String currency;
}
