package com.nexio.exercices.controller;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductDetailsRepository;
import com.nexio.exercices.utils.Utils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductDetailsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ProductDetailsRepository productDetailsRepository;


    @Test
    public void shouldShowPageOfDetailsOfProduct() {
        final Product existingProduct = Utils.generateProductWithDetails(true);
        when(productDetailsRepository.findByProductId(anyLong()))
                .thenReturn(Optional.of(existingProduct.getProductDetails()));

        assertThat(this.restTemplate.getForObject(
                String.format("http://localhost:%d/products/1/details", port),
                String.class
        ))
                .contains(existingProduct.getName())
                .contains(existingProduct.getProductDetails().getDescription());
    }

    @Test
    public void shouldReturn404IfProductIsNotFound() {
        when(productDetailsRepository.findByProductId(anyLong()))
                .thenReturn(Optional.empty());

        final ResponseEntity<String> result = this.restTemplate.getForEntity(
                String.format("http://localhost:%d/products/1/details", port),
                String.class);

        assertThat(result.getStatusCodeValue()).isEqualTo(404);
        assertThat(result.getBody()).contains("404");
    }

    @After
    public void resetMockBean() {
        reset(productDetailsRepository);
    }
}