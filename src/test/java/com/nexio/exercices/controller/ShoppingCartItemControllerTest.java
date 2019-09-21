package com.nexio.exercices.controller;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartItemControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Test
    public void shouldCreateNewShoppingCartItemDtoWithAQuantityOfOneAndReturn201()
            throws Exception {
        final Product existingProduct = DataGenerator.generateProduct(true);
        existingProduct.setId(7L);

        when(productRepository.findById(7L))
                .thenReturn(Optional.of(existingProduct));
        when(shoppingCartItemRepository.findByProductId(7L))
                .thenReturn(Optional.empty());

        mvc.perform(put("/shopping-cart/items")
                .content("{\"productId\": \"7\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.isNew").doesNotExist())
                .andExpect(jsonPath("$.productId", is(7)))
                .andExpect(jsonPath("$.quantity", is(1)));
    }

    @Test
    public void shouldIncreaseQuantityForExistingShoppingCartItemAndReturn200()
            throws Exception {
        final Product existingProduct = DataGenerator.generateProduct(true);
        existingProduct.setId(1L);
        final ShoppingCartItem existingShoppingCartItem =
                DataGenerator.generateShoppingCartItem(existingProduct);
        existingShoppingCartItem.setQuantity(3);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(existingProduct));
        when(shoppingCartItemRepository.findByProductId(1L))
                .thenReturn(Optional.of(existingShoppingCartItem));

        mvc.perform(put("/shopping-cart/items")
                .content("{\"productId\": \"1\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.isNew").doesNotExist())
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath( "$.quantity",is(4)));
    }

    @Test
    public void whenInvalidProductIdIsGivenThenItShouldReturnNotFound()
            throws Exception {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(put("/shopping-cart/items")
                .content("{\"productId\": \"2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }
}