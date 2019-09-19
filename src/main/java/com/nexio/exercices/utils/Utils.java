package com.nexio.exercices.utils;

import com.github.javafaker.Faker;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ProductDetails;

import java.math.BigDecimal;
import java.util.Locale;

///TODO rename to ContentGenerator or DataGenerator
public final class Utils {

    private static final Faker faker = Faker.instance(Locale.CANADA_FRENCH);

    private Utils() {
    }

    public static Product generateProductWithDetails(boolean isEdible) {
        final Product product = generateProduct(isEdible);
        product.setProductDetails(new ProductDetails(
                faker.lorem().sentence(5, 40),
                Boolean.valueOf(isEdible)
        ));

        return product;
    }

    public static Product generateProduct(boolean isEdible) {
        final String productName;
        if (isEdible) {
            productName = faker.food().ingredient();
        } else {
            productName = faker.book().title();
        }

        final BigDecimal price = BigDecimal.valueOf(
                faker.number().randomDouble(2, 1, 999)
        );

        return new Product(productName, price);
    }
}
