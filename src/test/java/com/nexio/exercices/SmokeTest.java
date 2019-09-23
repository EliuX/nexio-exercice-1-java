package com.nexio.exercices;

import com.nexio.exercices.controller.GeneralController;
import com.nexio.exercices.controller.ProductController;
import com.nexio.exercices.controller.ProductDetailsController;
import com.nexio.exercices.controller.ShoppingCartController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmokeTest {

    @Autowired
    private GeneralController generalController;

    @Autowired
    private ProductController productController;

    @Autowired
    private ProductDetailsController productDetailsController;

    @Autowired
    private ShoppingCartController shoppingCartController;

    @Test
    public void shouldLoadContext() {
        assertThat(generalController).isNotNull();
        assertThat(productController).isNotNull();
        assertThat(productDetailsController).isNotNull();
        assertThat(shoppingCartController).isNotNull();
    }
}
