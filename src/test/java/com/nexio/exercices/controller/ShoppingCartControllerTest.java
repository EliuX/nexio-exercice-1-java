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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;
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
@WithMockUser(username = "testuser")
public class ShoppingCartControllerTest {

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
                    argument.setCreatedDate(new Date());
                    argument.setLastModifiedDate(new Date());
                    argument.setId(47L);
                    return argument;
                });

        existingProduct = dataGenerator.generateProduct(true);
        existingProduct.setId(7L);

        existingShoppingCartItem = new ShoppingCartItem(existingProduct, 1);
        existingShoppingCartItem.setCreatedDate(new Date());
        existingShoppingCartItem.setLastModifiedDate(new Date());

        when(productRepository.findById(7L)).thenReturn(Optional.of(existingProduct));
        when(shoppingCartItemRepository.findByProductIdAndCurrentUser(7L))
                .thenReturn(Optional.of(existingShoppingCartItem));
    }

    @Test
    public void shouldCreateNewShoppingCartItemDtoWithAQuantityOfOneAndReturn201()
            throws Exception {
        when(shoppingCartItemRepository.findByProductIdAndCurrentUser(7L))
                .thenReturn(Optional.empty());

        mvc.perform(put("/shopping-cart/items")
                .content("{\"productId\": \"7\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.new").doesNotExist())
                .andExpect(jsonPath("$.product.id", is(7)))
                .andExpect(jsonPath("$.quantity", is(1)))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.lastModifiedDate").exists())
                .andExpect(jsonPath(
                        "$.totalPrice",
                        is(BigDecimal.valueOf(1)
                                .multiply(existingProduct.getPrice())
                                .doubleValue())
                ));

        verify(shoppingCartItemRepository, times(1))
                .save(new ShoppingCartItem(existingProduct, 1));
    }

    @Test
    public void shouldIncreaseQuantityForExistingShoppingCartItemAndReturn200()
            throws Exception {
        mvc.perform(put("/shopping-cart/items")
                .content("{\"productId\": \"7\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.new").doesNotExist())
                .andExpect(jsonPath("$.product.id", is(7)))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.lastModifiedDate").exists())
                .andExpect(jsonPath("$.totalPrice",
                        is(BigDecimal.valueOf(2).multiply(existingProduct.getPrice())
                                .doubleValue()))
                );
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

        when(shoppingCartItemRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(existingItems);

        final Product productFirstItem = existingItems.get(0).getProduct();

        mvc.perform(get("/shopping-cart/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(COUNT_OF_EXISTING_ITEMS)))
                .andExpect(jsonPath("$[0].product.id", is(productFirstItem.getId())))
                .andExpect(jsonPath("$[0].product.name", is(productFirstItem.getName())))
                .andExpect(jsonPath("$[0].product.price", is(productFirstItem.getPrice().doubleValue())))
                .andExpect(jsonPath(
                        "$[0].totalPrice",
                        is(BigDecimal.valueOf(existingItems.get(0).getQuantity())
                                .multiply(productFirstItem.getPrice())
                                .doubleValue())
                ))
                .andExpect(jsonPath(
                        "$[0].quantity",
                        is(existingItems.get(0).getQuantity())
                ));
    }

    @Test
    public void givenProductIdWithOneItemInShoppingCart_whenDelete_thenDeleteItemAndReturn200()
            throws Exception {
        mvc.perform(delete("/shopping-cart/items")
                .content("{\"productId\": \"7\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").exists())
                .andExpect(jsonPath("$.product.id", is(7)))
                .andExpect(jsonPath("$.product.name", is(existingProduct.getName())))
                .andExpect(jsonPath(
                        "$.product.price",
                        is((existingProduct.getPrice().doubleValue()))
                ))
                .andExpect(jsonPath("$.totalPrice", is(0d)))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.lastModifiedDate").exists())
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
        return dataGenerator.generateShoppingCartItem(product, "testuser");
    }

    @Test
    public void givenEmptyJsonRequest_whenAddItemOfProduct_thenReturn400() throws Exception {
        mvc.perform(put("/shopping-cart/items")
                .content("{ }")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNoItems_whenGetContent_thenReturnEmptyResponse() throws Exception {
        when(shoppingCartItemRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/shopping-cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.totalPrice").exists())
                .andExpect(jsonPath("$.totalPrice", is(0)));
    }

    @Test
    public void givenExistingItemsWithPrices_whenGetContent_thenReturnListOfItemsAndTotalPrice()
            throws Exception {
        final ShoppingCartItem item1 = generateShoppingCartItemToCalculateTotalPrice(
                2, BigDecimal.valueOf(15)
        );
        final ShoppingCartItem item2 = generateShoppingCartItemToCalculateTotalPrice(
                40, BigDecimal.valueOf(0.25)
        );
        final ShoppingCartItem item3 = generateShoppingCartItemToCalculateTotalPrice(
                1, BigDecimal.valueOf(9.50)
        );
        final ShoppingCartItem item4 = generateShoppingCartItemToCalculateTotalPrice(
                1, BigDecimal.valueOf(47.32)
        );
        final ShoppingCartItem item5 = generateShoppingCartItemToCalculateTotalPrice(
                1, BigDecimal.valueOf(0.50)
        );
        final ShoppingCartItem item6 = generateShoppingCartItemToCalculateTotalPrice(
                1, BigDecimal.valueOf(10)
        );

        when(shoppingCartItemRepository.findAll(any(Specification.class), any(Sort.class)))
                .thenReturn(Arrays.asList(item1, item2, item3, item4, item5, item6));

        mvc.perform(get("/shopping-cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.items", hasSize(6)))
                .andExpect(jsonPath("$.totalPrice", is(107.32)));
    }


    private ShoppingCartItem generateShoppingCartItemToCalculateTotalPrice(
            Integer quantity, BigDecimal productPrice
    ) {
        final ShoppingCartItem item = new ShoppingCartItem();
        item.setQuantity(quantity);

        final Product product = new Product();
        product.setPrice(productPrice);
        item.setProduct(product);

        return item;
    }
}