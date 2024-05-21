package com.qvl.gethomeweb.util.linepay;

import com.qvl.gethomeweb.dto.payment.CheckoutPaymentRequestForm;

public interface PaymentService {
    String sendRequestAPI(CheckoutPaymentRequestForm checkoutPaymentRequestForm);
}
