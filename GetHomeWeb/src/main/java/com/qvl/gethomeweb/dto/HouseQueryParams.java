package com.qvl.gethomeweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String orderBy;
    private String orderType;
    private Integer limit;
    private Integer offset;
}
