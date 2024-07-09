package com.qvl.gethomeweb.dto;

import lombok.Data;

@Data
public class CancelCheckRequest {
    private Integer rentId;
    private Integer houseId;
}
