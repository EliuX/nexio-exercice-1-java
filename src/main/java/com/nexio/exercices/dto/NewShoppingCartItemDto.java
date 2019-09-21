package com.nexio.exercices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewShoppingCartItemDto {

    private Long productId;

    public NewShoppingCartItemDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
