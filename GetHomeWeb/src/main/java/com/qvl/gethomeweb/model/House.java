package com.qvl.gethomeweb.model;

import com.qvl.gethomeweb.constant.Gender;
import com.qvl.gethomeweb.constant.HouseStatus;
import com.qvl.gethomeweb.constant.HouseType;
import lombok.Data;

import java.util.Date;

@Data
public class House {
    private Integer houseId;
    private HouseType houseType;
    private String address;
    private String imageUrl;
    private Integer pricePerMonth;
    private Gender gender;
    private HouseStatus status;
    private String description;
    private Date createdDate;
    private Date lastUpdateDate;
}
