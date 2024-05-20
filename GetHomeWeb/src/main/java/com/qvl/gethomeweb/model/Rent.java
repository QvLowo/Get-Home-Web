package com.qvl.gethomeweb.model;

import lombok.Data;

import java.util.Date;

@Data
public class Rent {
    private Integer rentId;
    private Integer userId;
    private Integer houseId;
    private String status;
    private Integer totalAmount;
    private Date startDate;
    private Date endDate;
    private Date createDate;
    private Date lastUpdateDate;
}
