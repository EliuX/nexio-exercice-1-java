package com.nexio.exercices.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

    @Autowired
    ProductController productController;

    @Autowired
    ProductDetailsController productDetailsController;

    @Test
    public void shouldLoadContext() {
        assertThat(productController).isNotNull();
        assertThat(productDetailsController).isNotNull();
    }

}
