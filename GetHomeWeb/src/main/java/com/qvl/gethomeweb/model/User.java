package com.qvl.gethomeweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer userId;
    private String userName;
    private String phone;
    //    隱藏密碼不傳給前端
    @JsonIgnore
    private String password;
    private String email;
    private Date createdDate;
    private Date lastUpdateDate;
}
