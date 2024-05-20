package com.qvl.gethomeweb.model;

import lombok.Data;

import java.util.Date;

@Data
public class RentInfo {
    private Integer rentInfoId;
    private Integer rentId;
    private Integer houseId;
    private Integer amount;
    private Integer month;
    private Date createDate;
    private Date lastUpdateDate;

}
