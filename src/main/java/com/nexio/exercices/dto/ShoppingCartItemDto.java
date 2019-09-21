package com.nexio.exercices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShoppingCartItemDto {

    private Long id;

    private Long productId;

    private String productName;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonIgnore
    public boolean isNew() {
        return getQuantity() == null || getQuantity() <= 1;
    }
}
