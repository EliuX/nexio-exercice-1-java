package com.nexio.exercices.dto;

import java.beans.Transient;

public class ShoppingCartItemDto {

    private Long id;

    private Long productId;

    private Integer quantity;

    public ShoppingCartItemDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isNew() {
        return getQuantity() == 1;
    }
}
