package com.nexio.exercices.utils;

import com.github.javafaker.Faker;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ProductDetails;
import com.nexio.exercices.model.ShoppingCartItem;

import java.math.BigDecimal;
import java.util.Locale;

public class DataGenerator {

    private final Faker faker;

    public DataGenerator() {
        faker = Faker.instance();
    }

    public DataGenerator(Locale locale) {
        this.faker = Faker.instance(locale);
    }

    public Product generateProductWithDetails(boolean isEdible) {
        final Product product = generateProduct(isEdible);
        product.setProductDetails(new ProductDetails(
                faker.lorem().sentence(5, 40),
                Boolean.valueOf(isEdible)
        ));

        return product;
    }

    public Product generateProduct(boolean isEdible) {
        final String productName;
        if (isEdible) {
            productName = faker.food().ingredient();
        } else {
            productName = faker.book().title();
        }

        final BigDecimal price = BigDecimal.valueOf(
                faker.number().randomDouble(2, 2, 999)
        );

        return new Product(productName, price);
    }

    public ShoppingCartItem generateShoppingCartItem(Product product) {
        Integer quantity = faker.number().numberBetween(1, 100);
        return new ShoppingCartItem(product, quantity);
    }
}
