package com.nexio.exercices.controller;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductRepository;
import com.nexio.exercices.persistence.ShoppingCartItemRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Test
    public void givenNoShoppingCartForExistingProduct_whenAddOneItemToShoppingCartOfProduct_returnElementWithOneItem()
            throws Exception {
        final Product existingProduct =
                DataGenerator.generateProductWithDetails(true);
        existingProduct.setId(7L);

        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(existingProduct));
        when(shoppingCartItemRepository.findByProductId(anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(put("/shopping-cart/add/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.productId", is(7)))
                .andExpect(jsonPath("$.quantity", is(1)));
    }

    @Test
    public void givenNoProduct_whenAddOneItemToShoppingCartOfProduct_returnNotFound()
            throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(put("/shopping-cart/add/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }
}