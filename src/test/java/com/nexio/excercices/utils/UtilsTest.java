package com.nexio.excercices.utils;

import com.nexio.excercices.model.Product;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class UtilsTest {

    @Test
    public void generateProduct_shouldGenerateValidProduct() {
        final Product product = Utils.generateProduct(true);

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
                product.getPrice().compareTo(BigDecimal.valueOf(10000))
        );
    }

    @Test
    public void generateProduct_shouldGenerateValidProductDetails() {
        final Product product = Utils.generateProduct(true);

        Assert.assertNotNull(
                "The productDetails should not be null",
                product.getProductDetails()
        );

        final int pdDescriptionLength = product.getProductDetails()
                .getDescription().length();
        Assert.assertTrue(
                "The product detail description should " +
                        "be between 3 and 500 chars",
                pdDescriptionLength >= 3 && pdDescriptionLength <= 500
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
    public void generateProduct_shouldGenerateAnEdibleProduct() {
        final Product product = Utils.generateProduct(true);

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
    public void generateProduct_shouldGenerateANonEdibleProduct() {
        final Product product = Utils.generateProduct(false);

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