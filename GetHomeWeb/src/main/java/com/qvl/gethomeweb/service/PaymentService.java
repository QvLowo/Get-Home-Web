package com.qvl.gethomeweb.service;

import com.qvl.gethomeweb.dto.payment.CheckoutPaymentRequestForm;

public interface PaymentService {
    String sendRequestAPI(CheckoutPaymentRequestForm checkoutPaymentRequestForm);

    String getTransactionId(CheckoutPaymentRequestForm checkoutPaymentRequestForm);

    String sendConfirmAPI(Integer rentId, String transactionId, String paymentId);
}
