package com.qvl.gethomeweb.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserLoginRequest {
//    電話號碼不可空白不可空字串，長度固定為10，且必須為0-9的數字組成
//    @Pattern(regexp = "^(?=.*[0-9])[\\d]{10}$")
    private String phone;
//        密碼不可空白不可空字串，長度固定為8-16，且必須包含大小寫字母及數字
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}$")
    private String password;
    private String code;
    private String uuid = "";
}
