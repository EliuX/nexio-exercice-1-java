package com.nexio.exercices.controller;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
import com.nexio.exercices.persistence.ProductRepository;
import com.nexio.exercices.persistence.ShoppingCartItemRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartItemControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private DataGenerator dataGenerator;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ShoppingCartItemRepository shoppingCartItemRepository;

    private Product existingProduct;

    private ShoppingCartItem existingShoppingCartItem;

    @Before
    public void mockShoppingCartItemRepositorySave() {
        when(shoppingCartItemRepository.save(ArgumentMatchers.any(ShoppingCartItem.class)))
                .thenAnswer(invocationOnMock -> {
                    final ShoppingCartItem argument = invocationOnMock.getArgument(0);
                    argument.setId(47L);
                    return argument;
                });

        existingProduct = dataGenerator.generateProduct(true);
        existingProduct.setId(7L);

        existingShoppingCartItem =
                dataGenerator.generateShoppingCartItem(existingProduct);
        existingShoppingCartItem.setQuantity(1);

        when(productRepository.findById(7L)).thenReturn(Optional.of(existingProduct));
        when(shoppingCartItemRepository.findByProductId(7L)).thenReturn(Optional.of(existingShoppingCartItem));
    }

    @Test
    public void shouldCreateNewShoppingCartItemDtoWithAQuantityOfOneAndReturn201()
            throws Exception {
        final Product existingProduct = dataGenerator.generateProduct(true);
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
                .andExpect(jsonPath("$.new").doesNotExist())
                .andExpect(jsonPath("$.productId", is(7)))
                .andExpect(jsonPath("$.quantity", is(1)));
    }

    @Test
    public void shouldIncreaseQuantityForExistingShoppingCartItemAndReturn200()
            throws Exception {
        final Product existingProduct = dataGenerator.generateProduct(true);
        existingProduct.setId(1L);
        final ShoppingCartItem existingShoppingCartItem =
                dataGenerator.generateShoppingCartItem(existingProduct);
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
                .andExpect(jsonPath("$.new").doesNotExist())
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.quantity", is(4)));
    }

    @Test
    public void whenInvalidProductIdIsGivenThenItShouldReturnNotFound()
            throws Exception {
        when(productRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(put("/shopping-cart/items")
                .content("{\"productId\": \"2\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldReturnAllShoppingCartItems()
            throws Exception {
        final int COUNT_OF_EXISTING_ITEMS = 10;
        final List<ShoppingCartItem> existingItems =
                Stream.generate(this::createNewShoppingCartItem)
                        .limit(COUNT_OF_EXISTING_ITEMS)
                        .collect(Collectors.toList());

        when(shoppingCartItemRepository.findAll()).thenReturn(existingItems);

        final Product productFirstItem = existingItems.get(0).getProduct();

        mvc.perform(get("/shopping-cart/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(COUNT_OF_EXISTING_ITEMS)))
                .andExpect(jsonPath("$[0].productId", is(productFirstItem.getId())))
                .andExpect(jsonPath("$[0].productName", is(productFirstItem.getName())))
                .andExpect(jsonPath("$[0].quantity", is(existingItems.get(0).getQuantity())));
    }

    @Test
    public void givenProductIdWithOneItemInShoppingCart_whenDelete_thenDeleteItemAndReturn200()
            throws Exception {
        mvc.perform(delete("/shopping-cart/items")
                .content("{\"productId\": \"7\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(7)))
                .andExpect(jsonPath("$.productName", is(existingProduct.getName())))
                .andExpect(jsonPath("$.quantity", is(0)));

        verify(shoppingCartItemRepository, times(1))
                .delete(existingShoppingCartItem);
    }

    @Test
    public void givenProductIdWithNoItemInShoppingCart_whenDelete_thenDoNoDeletionAndReturn404()
            throws Exception {
        mvc.perform(delete("/shopping-cart/items")
                .content("{\"productId\": \"8\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(shoppingCartItemRepository, times(0))
                .delete(ArgumentMatchers.any(ShoppingCartItem.class));
    }

    private ShoppingCartItem createNewShoppingCartItem() {
        final Product product = dataGenerator.generateProduct(false);
        return dataGenerator.generateShoppingCartItem(product);
    }
}