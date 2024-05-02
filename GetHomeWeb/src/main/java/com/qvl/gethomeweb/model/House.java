package com.qvl.gethomeweb.model;

import com.qvl.gethomeweb.constant.HouseType;
import lombok.Data;

import java.util.Date;

@Data
public class House {
    private Integer houseId;
    private HouseType houseType;
    private String address;
    private String imageUrl;
    private Integer price;
    private Integer utilities;
    private Integer square;
    private Integer managementCost;
    private String gender;
    private String status;
    private String description;
    private Date createdDate;
    private Date lastUpdateDate;
}
