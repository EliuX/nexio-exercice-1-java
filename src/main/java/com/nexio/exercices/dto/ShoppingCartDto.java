package com.nexio.exercices.dto;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartDto {

    private List<ShoppingCartItemDto> items;

    public ShoppingCartDto() {
    }

    public List<ShoppingCartItemDto> getItems() {
        return items;
    }

    public void setItems(List<ShoppingCartItemDto> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return getItems().stream()
                .map(ShoppingCartItemDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
