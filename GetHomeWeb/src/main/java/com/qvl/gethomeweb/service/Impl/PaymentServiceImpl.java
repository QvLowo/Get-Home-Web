package com.qvl.gethomeweb.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.qvl.gethomeweb.dao.HouseDao;
import com.qvl.gethomeweb.dao.RentDao;
import com.qvl.gethomeweb.dto.payment.CheckoutPaymentRequestForm;
import com.qvl.gethomeweb.dto.payment.ConfirmDataRequest;
import com.qvl.gethomeweb.model.RentInfo;
import com.qvl.gethomeweb.model.payment.ProductForm;
import com.qvl.gethomeweb.model.payment.ProductPackageForm;
import com.qvl.gethomeweb.model.payment.RedirectUrls;
import com.qvl.gethomeweb.service.PaymentService;
import com.qvl.gethomeweb.util.CallApi;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.UUID;

import static com.qvl.gethomeweb.constant.HouseStatus.RENTED;
import static com.qvl.gethomeweb.constant.HouseStatus.RESERVED;
import static com.qvl.gethomeweb.constant.RentStatus.COMPLETED;

@Component
public class PaymentServiceImpl implements PaymentService {
    private String LINE_PAY_CHANNEL_ID = "2005081797";
    private String LINE_PAY_CHANNEL_SECRET = "92c751655489b57e1cbdc5060e99d78b";
    private String LINE_PAY_REQUEST_URI = "/v3/payments/request";
    private String LINE_PAY_REQUEST_URL = "https://sandbox-api-pay.line.me/v3/payments/request";
    private String LINE_PAY_URL = "https://sandbox-api-pay.line.me/";
    private final static Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Autowired
    private CallApi callApi;
    @Autowired
    private RentDao rentDao;

    @Autowired
    private HouseDao houseDao;

    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
    }

    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }

    @Transactional
    @Override
    public String sendRequestAPI(CheckoutPaymentRequestForm checkoutPaymentRequestForm) {
        String paymentId = checkoutPaymentRequestForm.getOrderId();
        RentInfo rentInfo = rentDao.getRentInfoByPaymentId(paymentId);
        CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
        form.setAmount(BigDecimal.valueOf(rentInfo.getAmount()));
        form.setCurrency("TWD");
        form.setOrderId(paymentId);

        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package");
        productPackageForm.setName("get_home");
        productPackageForm.setAmount(BigDecimal.valueOf(rentInfo.getAmount()));

        ProductForm productForm = new ProductForm();
        productForm.setId(rentInfo.getHouseId().toString());
//        Line Pay付款畫面顯示名稱
        productForm.setName(rentInfo.getHouseName());
        productForm.setImageUrl(rentInfo.getImageUrl());
        productForm.setQuantity(BigDecimal.valueOf(rentInfo.getMonth()));
        productForm.setPrice(BigDecimal.valueOf(rentInfo.getAmount() / rentInfo.getMonth()));
        productPackageForm.setProducts(Lists.newArrayList(productForm));

        form.setPackages(Lists.newArrayList(productPackageForm));
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setConfirmUrl("http://localhost:8080/confirm-pay");
        redirectUrls.setCancelUrl("");
        form.setRedirectUrls(redirectUrls);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String nonce = UUID.randomUUID().toString();
            String body = mapper.writeValueAsString(form);
            String signature = encrypt(LINE_PAY_CHANNEL_SECRET, LINE_PAY_CHANNEL_SECRET + LINE_PAY_REQUEST_URI + body + nonce);
            JsonNode response = callApi.sendPostRequest(LINE_PAY_CHANNEL_ID , nonce, signature,LINE_PAY_REQUEST_URL, body);
            String apiCode = response.get("returnCode").asText();
            if (apiCode.equals("0000")) {
//               0000為linepay端驗證成功，取得response json中的連結及交易ID
                String url = response.get("info").get("paymentUrl").get("web").asText();
                String transactionId = response.get("info").get("transactionId").asText();
                //            付款連結出現後，將房屋狀態改為預訂中
                houseDao.updateHouseStatus(rentInfo.getHouseId(), RESERVED);
                rentDao.setTransactionId(paymentId, transactionId);
                return url;
            } else {
                log.warn("line pay api error: " + apiCode + " " + response.get("returnMessage").asText());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "line pay api error");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTransactionId(CheckoutPaymentRequestForm checkoutPaymentRequestForm) {
        String paymentId = checkoutPaymentRequestForm.getOrderId();
        RentInfo rentInfo = rentDao.getRentInfoByPaymentId(paymentId);
        CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();
        form.setAmount(BigDecimal.valueOf(rentInfo.getAmount()));
        form.setCurrency("TWD");
        form.setOrderId(paymentId);

        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package");
        productPackageForm.setName("get_home");
        productPackageForm.setAmount(BigDecimal.valueOf(rentInfo.getAmount()));

        ProductForm productForm = new ProductForm();
        productForm.setId(rentInfo.getHouseId().toString());
//        Line Pay付款畫面顯示名稱
        productForm.setName("get_home");
        productForm.setImageUrl("");
        productForm.setQuantity(BigDecimal.valueOf(rentInfo.getMonth()));
        productForm.setPrice(BigDecimal.valueOf(rentInfo.getAmount() / rentInfo.getMonth()));
        productPackageForm.setProducts(Lists.newArrayList(productForm));

        form.setPackages(Lists.newArrayList(productPackageForm));
        RedirectUrls redirectUrls = new RedirectUrls();
//        確認url，尚未設定先使用google替代
//        凌晨更新
        redirectUrls.setConfirmUrl("http://localhost:8080/confirm-pay");
        redirectUrls.setCancelUrl("");
        form.setRedirectUrls(redirectUrls);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String nonce = UUID.randomUUID().toString();
            String body = mapper.writeValueAsString(form);
            String signature = encrypt(LINE_PAY_CHANNEL_SECRET, LINE_PAY_CHANNEL_SECRET + LINE_PAY_REQUEST_URI + body + nonce);
            JsonNode response = callApi.sendPostRequest(LINE_PAY_CHANNEL_ID, nonce, signature, LINE_PAY_REQUEST_URL, body);
            String url = response.get("info").get("transactionId").asText();
            return url;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    @Override
    public String sendConfirmAPI(Integer rentId, String transactionId, String paymentId) {
        RentInfo rentInfo = rentDao.getRentInfoByPaymentId(paymentId);
        ConfirmDataRequest confirmDataRequest = new ConfirmDataRequest();
        confirmDataRequest.setAmount(BigDecimal.valueOf(rentInfo.getAmount()));
        confirmDataRequest.setCurrency("TWD");
        ObjectMapper mapper = new ObjectMapper();
        String confirmUri = "/v3/payments/" + transactionId + "/confirm";
        String confirmUrl =  LINE_PAY_URL + confirmUri;
        try {
            String body = mapper.writeValueAsString(confirmDataRequest);
            String nonce = UUID.randomUUID().toString();
            String signature = encrypt(LINE_PAY_CHANNEL_SECRET, LINE_PAY_CHANNEL_SECRET + confirmUri + body + nonce);
            JsonNode response = callApi.sendPostRequest(LINE_PAY_CHANNEL_ID, nonce, signature, confirmUrl, body);
            String apiCode = response.get("returnCode").asText();
            int paidAmount = rentDao.getPaid(rentId);
            int totalAmount = rentDao.getRentById(rentId).getTotalAmount();
            int balance = totalAmount - paidAmount;
            if (apiCode.equals("0000") || apiCode.equals("1169")) {
                houseDao.updateHouseStatus(rentInfo.getHouseId(), RENTED);
                rentDao.updateRentStatus(rentId, COMPLETED);
                rentDao.updateAccountPayable(rentId, balance);
                return apiCode;
            } else if (apiCode.equals("1172")) {
                log.warn("line pay api error: " + apiCode + " " + response.get("returnMessage").asText());
                rentDao.deleteRentInfo(paymentId);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "訂單交易序號重複，訂單已失效");
            } else {
                log.warn("line pay api error: " + apiCode + " " + response.get("returnMessage").asText());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "line pay api error");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}//end
