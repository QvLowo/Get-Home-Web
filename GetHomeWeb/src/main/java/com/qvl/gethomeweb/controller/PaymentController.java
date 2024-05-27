package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.dto.ResponseMessage;
import com.qvl.gethomeweb.dto.payment.CheckoutPaymentRequestForm;
import com.qvl.gethomeweb.dto.payment.ConfirmDataRequest;
import com.qvl.gethomeweb.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/request")
    public ResponseEntity<ResponseMessage> request(@RequestBody CheckoutPaymentRequestForm checkoutPaymentRequestForm) {
        String url = paymentService.sendRequestAPI(checkoutPaymentRequestForm);
        String transactionId = paymentService.getTransactionId(checkoutPaymentRequestForm);
        if (url != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(url, transactionId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<ResponseMessage> confirm(@RequestBody ConfirmDataRequest confirmDataRequest) {
        paymentService.sendConfirmAPI(confirmDataRequest.getTransactionId(), confirmDataRequest.getPaymentId());
        if (confirmDataRequest.getTransactionId() != null)
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("", "success"));
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
