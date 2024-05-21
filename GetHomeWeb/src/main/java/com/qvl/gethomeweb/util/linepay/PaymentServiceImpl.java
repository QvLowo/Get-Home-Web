package com.qvl.gethomeweb.util.linepay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.qvl.gethomeweb.constant.Payment;
import com.qvl.gethomeweb.dao.RentDao;
import com.qvl.gethomeweb.dto.payment.CheckoutPaymentRequestForm;
import com.qvl.gethomeweb.dto.payment.ProductForm;
import com.qvl.gethomeweb.dto.payment.ProductPackageForm;
import com.qvl.gethomeweb.dto.payment.RedirectUrls;
import com.qvl.gethomeweb.model.RentInfo;
import com.qvl.gethomeweb.util.CallApi;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private CallApi callApi;
    @Autowired
    private RentDao rentDao;

    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
    }

    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }

    @Override
    public String sendRequestAPI(CheckoutPaymentRequestForm checkoutPaymentRequestForm) {
        String paymentId = checkoutPaymentRequestForm.getOrderId();
        RentInfo rentInfo = rentDao.getRentInfoById(paymentId);
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
        productForm.setName("house_name");
        productForm.setImageUrl("");
        productForm.setQuantity(BigDecimal.valueOf(rentInfo.getMonth()));
        productForm.setPrice(BigDecimal.valueOf(rentInfo.getAmount()/rentInfo.getMonth()));
        productPackageForm.setProducts(Lists.newArrayList(productForm));

        form.setPackages(Lists.newArrayList(productPackageForm));
        RedirectUrls redirectUrls = new RedirectUrls();
//        確認url，尚未設定先使用google替代
        redirectUrls.setConfirmUrl("http://www.google.com");
        redirectUrls.setCancelUrl("");
        form.setRedirectUrls(redirectUrls);

        Payment payment = new Payment();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String nonce = UUID.randomUUID().toString();
            String body = mapper.writeValueAsString(form);
            String signature = encrypt(payment.getChannelSecret(), payment.getChannelSecret() + payment.getRequestUri() + body + nonce);
            JsonNode response = callApi.sendPostRequest(payment.getChannelId(), nonce, signature, payment.getRequestHttpsUri(), body);
            System.out.println(body);
            System.out.println(nonce);
            System.out.println(signature);
           String url = response.get("info").get("paymentUrl").get("web").asText();
            return url;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}//end

