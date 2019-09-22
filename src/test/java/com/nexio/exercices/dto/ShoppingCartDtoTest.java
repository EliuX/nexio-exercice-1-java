package com.nexio.exercices.dto;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ShoppingCartDtoTest {

    @Test
    public void shouldCalculateTotalBasedOnTotalOfItemPrices() {
        final ShoppingCartItemDto itemDto1 = generateShoppingCartItemDtoToCalculateTotalPrice(
                1, BigDecimal.valueOf(47.40)
        );
        final ShoppingCartItemDto itemDto2 = generateShoppingCartItemDtoToCalculateTotalPrice(
                1, BigDecimal.valueOf(10.60)
        );
        final ShoppingCartItemDto itemDto3 = generateShoppingCartItemDtoToCalculateTotalPrice(
                1, BigDecimal.valueOf(2.50)
        );
        final ShoppingCartItemDto itemDto4 = generateShoppingCartItemDtoToCalculateTotalPrice(
                1, BigDecimal.valueOf(39.50)
        );

        final ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setItems(Arrays.asList(itemDto1, itemDto2, itemDto3, itemDto4));

        assertThat(
                shoppingCartDto.getTotalPrice().doubleValue(),
                equalTo(BigDecimal.valueOf(100).doubleValue())
        );
    }

    private ShoppingCartItemDto generateShoppingCartItemDtoToCalculateTotalPrice(
            Integer quantity, BigDecimal productPrice
    ){
        final ShoppingCartItemDto itemDto = new ShoppingCartItemDto();
        itemDto.setQuantity(quantity);

        final ProductDto productDto = new ProductDto();
        productDto.setPrice(productPrice);
        itemDto.setProduct(productDto);

        return itemDto;
    }
}