package com.qvl.gethomeweb.constant;

import lombok.Data;

import java.util.UUID;
@Data
public class Payment {
    public String channelId = "your channel id";
   public String ChannelSecret = "your channel secret";
   public String requestUri = "/v3/payments/request";
   public String requestUrl="https://sandbox-api-pay.line.me/v3/payments/request";
   public String domain = "https://sandbox-api-pay.line.me/";

}
