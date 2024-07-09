package com.qvl.gethomeweb.model.payment;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductForm {
    private String id;
    private String name;
    private String imageUrl;
    private BigDecimal quantity;
    private BigDecimal price;
}
