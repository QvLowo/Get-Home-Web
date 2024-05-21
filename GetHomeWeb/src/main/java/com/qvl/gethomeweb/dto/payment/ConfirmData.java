package com.qvl.gethomeweb.dto.payment;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ConfirmData {
    private BigDecimal amount;
    private String currency;
}
