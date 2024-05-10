package com.qvl.gethomeweb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RentRequest {
    @NotNull
    private Integer month;
    private String status;
}
