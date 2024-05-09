package com.qvl.gethomeweb.controller;

import com.qvl.gethomeweb.dto.UserLoginRequest;
import com.qvl.gethomeweb.dto.UserRegisterRequest;
import com.qvl.gethomeweb.model.User;
import com.qvl.gethomeweb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    房東註冊
    @PostMapping("/users/register/landlord")
    public ResponseEntity<User> landlordRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
//        密碼轉成hash儲存
        String hashedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(hashedPassword);

        Integer userId = userService.landLordRegister(userRegisterRequest);
        User user = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

//    租客註冊
    @PostMapping("/users/register/tenant")
    public ResponseEntity<User> tenantRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
//        密碼轉成hash儲存
        String hashedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(hashedPassword);

        Integer userId = userService.tenantRegister(userRegisterRequest);
        User user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

//    登入功能
    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        User user = userService.login(userLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
