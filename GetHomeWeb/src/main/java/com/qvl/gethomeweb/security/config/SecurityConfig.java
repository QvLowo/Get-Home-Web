package com.qvl.gethomeweb.security.config;

import com.qvl.gethomeweb.security.filter.JwtAuthenticationFilter;
import com.qvl.gethomeweb.security.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
//        密碼使用BCrypt演算法轉成Hash儲存
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                設置無狀態登入(jwt所以不需要session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                因使用jwt，所以csrf關閉
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests
//                        所有人都可以訪問登入、註冊頁面、取得驗證碼
                        .requestMatchers( "/sign-up/**", "/OAuth2/**", "/users/**", "/userInfo", "/code").permitAll()
//                        所有人都可以訪問swagger api頁面
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        會員相關資料需登入才可訪問頁面
                        .requestMatchers("/members/**").authenticated()
//                        租客相關限制租客權限
                        .requestMatchers("/tenants/**").hasRole("TENANT")
//                        付款頁面限制租客權限
                        .requestMatchers("/payments/**").hasAnyRole("TENANT")

//                       房屋瀏覽頁面允許所有人訪問 (一次很多間所以是houses)
                        .requestMatchers("/houses").permitAll()

//                         房屋詳情、租賃頁面限登入才可訪問
                        .requestMatchers("/houses/**").authenticated()
                        .requestMatchers("/rents","/rent/**").authenticated()

//                        房屋相關新增、修改、刪除等功能限制為房東權限 (一次一間所以是house)
                        .requestMatchers("/landlords/**").hasRole("LANDLORD")
                        .requestMatchers("/house/**").hasRole("LANDLORD")

//                        根據deny-by-default設計，避免意外暴露風險，所以預設未設定權限的頁面皆禁止訪問
                        .anyRequest().denyAll())
//                jwt相關filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//              記錄使用者登入filter (物件實體化 讓filter僅執行一次、若Autowired注入會導致跑兩次)
                .addFilterBefore(new SecurityFilter(), BasicAuthenticationFilter.class)
//                cors跨域設定
                .cors(cors -> cors.configurationSource(createCorsConfig()));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return webSecurity -> webSecurity.ignoring().requestMatchers("/OAuth2/**", "/users/login","/code","/swagger-ui/**", "/v3/api-docs/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //    CORS設定
    private CorsConfigurationSource createCorsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://127.0.0.1:8080", "http://127.0.0.1:8080/**","http://localhost:8080","http://localhost:8088/**", "https://images.pexels.com", "https://api.imgur.com/3/image"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
//       允許前端帶上cookie
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

//        CORS config套用到所有路徑
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
