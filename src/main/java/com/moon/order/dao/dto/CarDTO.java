package com.moon.order.dao.dto;

import lombok.Data;

@Data
public class CarDTO {

    /* 商品id */
    private String productId;

    /* 购买数量 */
    private Integer productQuantity;


    public CarDTO() {
    }

    public CarDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
