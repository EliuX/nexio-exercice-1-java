package com.nexio.exercices.dto;

import javax.validation.constraints.NotNull;

public class ShoppingCartItemChangeDto {

    @NotNull
    private Long productId;

    public ShoppingCartItemChangeDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
