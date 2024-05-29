package com.qvl.gethomeweb.security.config;

import com.qvl.gethomeweb.security.filter.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

//設定用class
@Configuration
//spring security客製化設定
@EnableWebSecurity
//方法層級設定
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
//        密碼使用BCrypt演算法轉成Hash儲存
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                設定session產生機制
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
//                設定csrf防禦惡意的POST/PUT/DELETE request，前端需帶上X-XSRF-TOKEN才可以請求
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .csrfTokenRequestHandler(createCsrfCHandler())
//                        公開的登入、註冊頁面忽略csrf防禦
                                .ignoringRequestMatchers("/users/login", "/users/register/*")
//                        openapi-swagger3忽略csrf防禦
                                .ignoringRequestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
//                        所有人都可以訪問登入、註冊頁面
                                .requestMatchers("/users/login", "/users/register/*").permitAll()
//                        所有人都可以訪問swagger api頁面
                                .requestMatchers("/swagger-ui/**",
                                        "/v3/api-docs/**").permitAll()
//                        房屋相關新增、修改、刪除等功能限制為房東權限
                                .requestMatchers("/house/**").hasRole("LANDLORD")
//                         房屋查詢功能限登入才可訪問
                                .requestMatchers("/houses/**").hasAnyRole("TENANT", "LANDLORD")

                                .requestMatchers("/users/**").hasRole("TENANT")
//                        根據deny-by-default設計，避免意外暴露風險，所以預設未設定權限的頁面皆禁止訪問
                                .anyRequest().denyAll()
                )
                .addFilterBefore(new SecurityFilter(), BasicAuthenticationFilter.class)
//                cors跨域設定
                .cors(cors -> cors
                        .configurationSource(createCorsConfig()))
                .build();
    }

    private CsrfTokenRequestAttributeHandler createCsrfCHandler() {
        CsrfTokenRequestAttributeHandler csrfHandler = new CsrfTokenRequestAttributeHandler();
        csrfHandler.setCsrfRequestAttributeName(null);
        return csrfHandler;
    }

    //    CORS設定，目前允許所有，未來待更新細部設定增加安全性
    private CorsConfigurationSource createCorsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
//        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
