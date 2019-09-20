package com.nexio.exercices.utils;

import com.nexio.exercices.model.Product;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class DataGeneratorTest {

    @Test
    public void generateProduct_shouldGenerateValidProduct() {
        final Product product = DataGenerator.generateProduct(true);

        final int productNameLength = product.getName().length();
        Assert.assertTrue(
                "The name should not have less than 1 character",
                productNameLength >= 1
        );
        Assert.assertTrue(
                "The name should not have more than 100 character",
                productNameLength <= 100
        );

        Assert.assertEquals(
                "The price should have 2 decimal values",
                2,
                product.getPrice().scale()
        );

        Assert.assertEquals("The price should have no more than 4 digits",
                -1,
                product.getPrice().compareTo(BigDecimal.valueOf(999))
        );
    }

    @Test
    public void generateProductWithDetails_shouldGenerateValidProductDetails() {
        final Product product = DataGenerator.generateProductWithDetails(true);

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

        Assert.assertEquals(
                "The productDetails should have a reference to the product",
                product,
                product.getProductDetails().getProduct()
        );
    }

    @Test
    public void generateProductWithDetails_shouldGenerateAnEdibleProduct() {
        final Product product = DataGenerator.generateProductWithDetails(true);

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
        final Product product = DataGenerator.generateProductWithDetails(false);

        Assert.assertFalse(
                "The product should not be edible",
                product.getProductDetails().getEdible()
        );

        Assert.assertFalse(
                "The product name should not be a zone of memory",
                product.getName().startsWith("com.github.javafaker")
        );
    }
}