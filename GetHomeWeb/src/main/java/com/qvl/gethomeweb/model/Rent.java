package com.qvl.gethomeweb.model;

import lombok.Data;

import java.util.Date;

@Data
public class Rent {
    private Integer rentId;
    private Integer userId;
    private Integer month;
    private Integer totalAmount;
    private String status;
    private Date createDate;
    public Date UpdateDate;
}
