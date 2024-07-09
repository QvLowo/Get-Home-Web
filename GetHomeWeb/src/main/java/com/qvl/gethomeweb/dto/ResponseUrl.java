package com.qvl.gethomeweb.dto;

import lombok.Data;

@Data
public class ResponseUrl {
    private String paymentUrl;
    public ResponseUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
