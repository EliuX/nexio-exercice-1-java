package com.nexio.exercices.dto;

import com.github.javafaker.Faker;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ShoppingCartItemDtoTest {

    final DataGenerator dataGenerator = new DataGenerator();

    private ProductDto productDto;

    @Before
    public void setup() {
        productDto = dataGenerator.generateProductDto(true);
    }

    @Test
    public void givenProductWithNullPrice_whenGetTotalPrice_thenReturn0() {
        final ShoppingCartItemDto dto = new ShoppingCartItemDto();
        productDto.setPrice(null);
        dto.setProduct(productDto);
        dto.setQuantity(2);

        assertThat(dto.getTotalPrice().doubleValue(), equalTo(0d));
    }

    @Test
    public void givenNullQuantity_whenGetTotalPrice_thenReturn0() {
        final ShoppingCartItemDto dto = new ShoppingCartItemDto();
        dto.setProduct(productDto);
        dto.setQuantity(null);

        assertThat(dto.getTotalPrice().doubleValue(), equalTo(0d));
    }

    @Test
    public void givenNullProduct_whenGetTotalPrice_thenReturn0() {
        final ShoppingCartItemDto dto = new ShoppingCartItemDto();
        dto.setProduct(null);
        dto.setQuantity(1);

        assertThat(dto.getTotalPrice().doubleValue(), equalTo(0d));
    }

    @Test
    public void givenProductWithPrice_whenGetTotalPrice_returnQuantityMultipliedPerPrice() {
        final ShoppingCartItemDto dto = new ShoppingCartItemDto();
        dto.setProduct(productDto);
        dto.setQuantity(7);

        assertThat(
                dto.getTotalPrice(),
                equalTo(BigDecimal.valueOf(7).multiply(productDto.getPrice()))
        );
    }
}