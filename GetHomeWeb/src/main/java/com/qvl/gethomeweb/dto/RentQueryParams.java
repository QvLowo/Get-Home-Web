package com.qvl.gethomeweb.dto;

import lombok.Data;

@Data
public class RentQueryParams {
    private Integer userId;
    private Integer houseId;
    private Integer rentId;
    private String orderBy;
    private String orderType;
    private Integer limit;
    private Integer offset;
}
