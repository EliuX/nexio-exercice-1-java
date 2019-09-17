package com.nexio.excercices.utils;

import com.github.javafaker.Faker;
import com.nexio.excercices.model.Product;
import com.nexio.excercices.model.ProductDetails;

import java.math.BigDecimal;
import java.util.Locale;

public final class Utils {

    private static final Faker faker = Faker.instance(Locale.CANADA_FRENCH);

    private Utils() {
    }

    public static Product generateProduct(boolean isEdible) {
        final ProductDetails productDetails = new ProductDetails(
                faker.lorem().characters(3, 500),
                Boolean.valueOf(isEdible)
        );

        final String productName;
        if (isEdible) {
            productName = faker.food().ingredient();
        } else {
            productName = faker.book().title();
        }

        final BigDecimal price = BigDecimal.valueOf(
                faker.number().randomDouble(2, 1, 9999)
        );

        return new Product(productName, price, productDetails);
    }
}
