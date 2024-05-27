package com.qvl.gethomeweb.model.payment;

import lombok.Data;

@Data
public class RedirectUrls {
    private String confirmUrl;
    private String cancelUrl;
    private String confirmUrlType;
}
