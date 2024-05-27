package com.qvl.gethomeweb.dto.payment;

import com.qvl.gethomeweb.model.payment.ProductPackageForm;
import com.qvl.gethomeweb.model.payment.RedirectUrls;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CheckoutPaymentRequestForm {
    private BigDecimal amount;
    private String currency;
    private String orderId;
    private List<ProductPackageForm> packages;
    private RedirectUrls redirectUrls;
}
