package com.nexio.exercices.controller;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductRepository;
import com.nexio.exercices.utils.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ProductRepository productRepository;

    @Test
    public void shouldNotHaveAnyProductsToShow() {
        assertThat(this.restTemplate.getForObject(String.format("http://localhost:%d/products", port), String.class))
                .contains("Pas de produits")
                .doesNotContain("produits disponibles");
    }

    @Test
    public void shouldHaveListed5Products() {
        int COUNT_OF_PRODUCTS_TO_ADD = 5;
        final List<Product> products = Stream.generate(() -> Utils.generateProductWithDetails(false))
                .limit(COUNT_OF_PRODUCTS_TO_ADD)
                .collect(Collectors.toList());
        when(productRepository.findAll()).thenReturn(products);

        assertThat(this.restTemplate.getForObject(String.format("http://localhost:%d/products", port), String.class))
                .doesNotContain("Pas de produits")
                .contains(String.format("Il y à %d produits disponibles", COUNT_OF_PRODUCTS_TO_ADD));

        productRepository.deleteAll();
    }
}