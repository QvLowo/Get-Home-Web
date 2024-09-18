package com.qvl.gethomeweb.controller.OAuth2;

import com.qvl.gethomeweb.dto.ExchangeTokenRequest;
import com.qvl.gethomeweb.dto.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/OAuth2")
@Tag(name = "Google OAuth2.0登入API")
public class GoogleOAuthController {

    //google 相關參數
    private String GOOGLE_CLIENT_ID = "your_client_id";

    private String GOOGLE_CLIENT_SECRET = "your_client_secret";


    private String GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth";

    private String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    //    使用者將跳轉至google認證中心頁面
    @Operation(summary="跳轉至Google認證中心")
    @GetMapping("/google/buildAuthUrl")
    public String buildAuthUrl() {
        // Google 提供的 auth url，使用者點擊此 url 之後，可以開始 OAuth 2.0 的授權流程
        String authUrl = GOOGLE_AUTH_URL;

        // 將google的請求參數與 auth url拼接
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(authUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", GOOGLE_CLIENT_ID)
                .queryParam("scope", "profile+email+openid")
                .queryParam("redirect_uri", "http://localhost:8080/login")
                .queryParam("state", generateRandomState())
                .queryParam("access_type", "offline");

        return uriBuilder.toUriString();
    }

    //    code、client_id、client_secret 的值傳給 Google 認證中心，得到 access_token 的值
    @Operation(summary="從Google認證中心取得access_token")
    @PostMapping("/google/exchangeToken")
    public String exchangeToken(@RequestBody ExchangeTokenRequest exchangeTokenRequest) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String tokenUrl = GOOGLE_TOKEN_URL;

        // 帶上 request body 中的請求參數
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", GOOGLE_CLIENT_ID);
        body.add("client_secret", GOOGLE_CLIENT_SECRET);
        body.add("code", exchangeTokenRequest.getCode());
        body.add("redirect_uri", "http://localhost:8080/login");

        // 發送請求
        String result;
        try {
            result = restTemplate.postForObject(
                    tokenUrl,
                    new HttpEntity<>(body, headers),
                    String.class
            );
        } catch (Exception e) {
            result = e.toString();
        }
//      得到access_token
        return result;
    }

    // 使用 access_token，取得登入者在 Google 中的使用者資料
    @Operation(summary="取得在Google儲存的使用者資料")
    @GetMapping("/google/getGoogleUser")
    public String getGoogleUser(@RequestParam String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        // call Google 的 api，取得使用者在 Google 中的基本資料
        String url = "https://www.googleapis.com/oauth2/v2/userinfo";

        // 發送請求
        String result;
        try {
            result = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            String.class
                    )
                    .getBody();

        } catch (Exception e) {
            result = e.toString();
        }
//      得到使用者資料
        return result;
    }

    // 使用 refresh_token，去和 Google 換發一個新的 access_token
    @Operation(summary="換發新的access_token")
    @PostMapping("/google/refreshToken")
    public String refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // 使用 Google 所提供的 token url，傳遞 refresh_token 的值過去，即可取得到新的 access token
        String tokenUrl = GOOGLE_TOKEN_URL;

        // 填寫 request body 中的請求參數
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", GOOGLE_CLIENT_ID);
        body.add("client_secret", GOOGLE_CLIENT_SECRET);
        body.add("refresh_token", refreshTokenRequest.getRefreshToken());

        // 發送請求
        String result;
        try {
            result = restTemplate.postForObject(
                    tokenUrl,
                    new HttpEntity<>(body, headers),
                    String.class
            );
        } catch (Exception e) {
            result = e.toString();
        }

        return result;
    }

    private String generateRandomState() {
        SecureRandom sr = new SecureRandom();
        byte[] data = new byte[6];
        sr.nextBytes(data);
        return Base64.getUrlEncoder().encodeToString(data);
    }
}
