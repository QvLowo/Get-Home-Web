package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.dto.ResponseMessage;
import com.qvl.gethomeweb.dto.payment.CheckoutPaymentRequestForm;
import com.qvl.gethomeweb.dto.payment.ConfirmDataRequest;
import com.qvl.gethomeweb.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@Tag(name="LinePay支付API")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "租客請求支付，取得付款連結及交易ID")
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

    @Operation(summary = "LinePay回傳付款結果，成功後台會入帳")
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
