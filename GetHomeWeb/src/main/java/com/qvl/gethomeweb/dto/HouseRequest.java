package com.qvl.gethomeweb.dto;

import com.qvl.gethomeweb.constant.Gender;
import com.qvl.gethomeweb.constant.HouseStatus;
import com.qvl.gethomeweb.constant.HouseType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
//驗證前端傳入的房屋資料用，以及加入@NotNull避免前端未填寫參數造成後端無法處理
// 使前端無法隨意更改houseId、createdDate、lastUpdateDate
public class HouseRequest {
    @NotNull
    private HouseType houseType;
    @NotNull
    private String address;
    @NotNull
    private String imageUrl;
    @NotNull
    private Integer pricePerMonth;
    private Gender gender;
    private HouseStatus status;
    private String description;
}
