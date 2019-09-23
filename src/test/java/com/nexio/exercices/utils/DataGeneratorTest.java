package com.nexio.exercices.utils;

import com.nexio.exercices.dto.ProductDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataGeneratorTest {

    @Autowired
    private DataGenerator dataGenerator;

    @Test
    public void generateProduct_shouldGenerateValidProduct() {
        final Product product = dataGenerator.generateProduct(true);

        final int productNameLength = product.getName().length();
        Assert.assertTrue(
                "The name should not have less than 1 character",
                productNameLength >= 1
        );
        Assert.assertTrue(
                "The name should not have more than 100 character",
                productNameLength <= 100
        );

        assertEquals(
                "The price should have 2 decimal values",
                2,
                product.getPrice().scale()
        );

        assertEquals("The price should have no more than 4 digits",
                -1,
                product.getPrice().compareTo(BigDecimal.valueOf(999))
        );
    }

    @Test
    public void generateProductWithDetails_shouldGenerateValidProductDetails() {
        final Product product = dataGenerator.generateProductWithDetails(true);

        Assert.assertNotNull(
                "The product should not be null",
                product
        );


        Assert.assertNotNull(
                "The productDetails should not be null",
                product.getProductDetails()
        );

        final int pdDescriptionLength = product.getProductDetails()
                .getDescription().length();
        Assert.assertTrue(
                "The product detail description should have no less than 3 chars",
                pdDescriptionLength >= 3
        );
        Assert.assertTrue(
                "The product detail description should have no more than 500 chars",
                pdDescriptionLength <= 500
        );

        Assert.assertNotNull(
                "The productDetails should not a null value for edible",
                product.getProductDetails().getEdible()
        );

        assertEquals(
                "The productDetails should have a reference to the product",
                product,
                product.getProductDetails().getProduct()
        );
    }

    @Test
    public void generateProductWithDetails_shouldGenerateAnEdibleProduct() {
        final Product product = dataGenerator.generateProductWithDetails(true);

        Assert.assertTrue(
                "The product should be edible",
                product.getProductDetails().getEdible()
        );

        Assert.assertFalse(
                "The product name should not be a zone of memory",
                product.getName().startsWith("com.github.javafaker")
        );
    }

    @Test
    public void generateProductWithDetails_shouldGenerateANonEdibleProduct() {
        final Product product = dataGenerator.generateProductWithDetails(false);

        Assert.assertFalse(
                "The product should not be edible",
                product.getProductDetails().getEdible()
        );

        Assert.assertFalse(
                "The product name should not be a zone of memory",
                product.getName().startsWith("com.github.javafaker")
        );
    }

    @Test
    public void generateShoppingCartItem_shouldGenerateValidElement() {
        final Product product = dataGenerator.generateProduct(true);
        final ShoppingCartItem shoppingCartItem =
                dataGenerator.generateShoppingCartItem(product, "user");

        Assert.assertNotNull(
                "The generated shopping cart item should not be null",
                shoppingCartItem
        );

        Assert.assertFalse(
                "The quantity should not be equals or lower to 1",
                shoppingCartItem.getQuantity() <= 0
        );

        Assert.assertFalse(
                "The quantity should not be bigger than 100",
                shoppingCartItem.getQuantity() > 100
        );

        Assert.assertNotNull(
                "The product should not be null",
                shoppingCartItem.getProduct()
        );

        Assert.assertNull(
                "The username should not be initially set",
                shoppingCartItem.getUsername()
        );
    }

    @Test
    public void shouldGenerateProductDtoBasedOnAttributesOfTheProduct() {
        final ProductDto productDto = dataGenerator.generateProductDto(true);

        assertNotNull(productDto.getId());
        assertNotNull(productDto.getId() > 0);

        assertNotNull(productDto.getName());
        assertNotNull(productDto.getName().isEmpty());

        assertNotNull(productDto.getPrice());
        assertEquals(1, productDto.getPrice().compareTo(BigDecimal.ZERO));
    }
}