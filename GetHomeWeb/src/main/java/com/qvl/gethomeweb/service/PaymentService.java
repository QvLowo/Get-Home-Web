package com.qvl.gethomeweb.service;

import com.qvl.gethomeweb.dto.payment.CheckoutPaymentRequestForm;

public interface PaymentService {
    String sendRequestAPI(CheckoutPaymentRequestForm checkoutPaymentRequestForm);
    String getTransactionId(CheckoutPaymentRequestForm checkoutPaymentRequestForm);
    void sendConfirmAPI(String transactionId, String paymentId);
}
