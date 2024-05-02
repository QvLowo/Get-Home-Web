package com.qvl.gethomeweb.dto;

import com.qvl.gethomeweb.constant.Gender;
import com.qvl.gethomeweb.constant.HouseStatus;
import com.qvl.gethomeweb.constant.HouseType;
import lombok.Data;

@Data
public class HouseQueryParams {
    private HouseType houseType;
    private String search;
    private Gender gender;
    private HouseStatus status;
}
