package com.nexio.exercices.utils;

import com.github.javafaker.Faker;
import com.nexio.exercices.dto.ProductDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ProductDetails;
import com.nexio.exercices.model.ShoppingCartItem;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;

public class DataGenerator {

    private final Faker faker;

    private final ModelMapper modelMapper;

    public DataGenerator() {
        faker = Faker.instance();
        modelMapper = new ModelMapper();
    }

    public DataGenerator(Locale locale, ModelMapper modelMapper) {
        this.faker = Faker.instance(locale);
        this.modelMapper = modelMapper;
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
                faker.number().randomDouble(2, 1, 999)
        ).setScale(2);

        return new Product(productName, price);
    }

    public ProductDto generateProductDto(boolean isEdible) {
        ProductDto result = modelMapper.map(generateProduct(isEdible), ProductDto.class);
        if (!Optional.ofNullable(result.getId()).isPresent()) {
            result.setId(1L);
        }
        return result;
    }

    public ShoppingCartItem generateShoppingCartItem(Product product, String username) {
        final Integer quantity = faker.number().numberBetween(1, 100);
        return new ShoppingCartItem(product, quantity);
    }
}
