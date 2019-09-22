package com.nexio.exercices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Optional;

public class ShoppingCartItemDto {

    private Long id;

    private ProductDto product;

    private Integer quantity;

    public ShoppingCartItemDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
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

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(
                Optional.ofNullable(getQuantity())
                        .orElse(0)
        ).multiply(
                Optional.ofNullable(product)
                        .map(ProductDto::getPrice)
                        .orElse(BigDecimal.ZERO)
        );
    }
}
