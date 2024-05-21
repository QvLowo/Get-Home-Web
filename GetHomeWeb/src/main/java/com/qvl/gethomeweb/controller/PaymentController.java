package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.dto.ResponseUrl;
import com.qvl.gethomeweb.dto.payment.CheckoutPaymentRequestForm;
import com.qvl.gethomeweb.util.linepay.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private PaymentServiceImpl paymentServiceImpl;

    @PostMapping("/request")
    public ResponseEntity<ResponseUrl> request(@RequestBody CheckoutPaymentRequestForm checkoutPaymentRequestForm) {
        String url = paymentServiceImpl.sendRequestAPI(checkoutPaymentRequestForm);
        if (url ==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseUrl(url));
        }
    }
}
