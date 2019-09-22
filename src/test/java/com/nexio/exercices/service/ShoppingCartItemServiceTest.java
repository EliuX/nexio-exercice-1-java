package com.nexio.exercices.service;

import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
import com.nexio.exercices.persistence.ShoppingCartItemRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithMockUser(username = "user")
public class ShoppingCartItemServiceTest {

    @Autowired
    private ShoppingCartItemService shoppingCartItemService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private DataGenerator dataGenerator;

    @Before
    public void mockShoppingCartItemRepositorySave() {
        when(shoppingCartItemRepository.save(ArgumentMatchers.any(ShoppingCartItem.class)))
                .thenAnswer(invocationOnMock -> {
                    final ShoppingCartItem argument = invocationOnMock.getArgument(0);
                    argument.setId(47L);
                    return argument;
                });
    }

    @Test
    public void shouldCreateNewShoppingCartItemForTheCurrentUser() {
        final Product product = dataGenerator.generateProduct(true);
        product.setId(7L);

        when(productService.findProductById(anyLong()))
                .thenReturn(Optional.of(product));
        when(shoppingCartItemRepository.findByProductIdAndUsername(7L, "users"))
                .thenReturn(Optional.empty());

        final Optional<ShoppingCartItemDto> shoppingCartItemDto
                = shoppingCartItemService.addOneItemOfProduct(7L);

        assertNotNull("The result should not be null", shoppingCartItemDto);
        assertTrue("The result should not be empty", shoppingCartItemDto.isPresent());
        assertEquals(shoppingCartItemDto.get().getProduct().getId(), product.getId());
        assertEquals(shoppingCartItemDto.get().getProduct().getName(), product.getName());
        assertThat(shoppingCartItemDto.get().getQuantity(), equalTo(1));
    }

    @Test
    public void shouldIncrementQuantifyOfExistingShoppingCartItemForTheCurrentUser() {
        final Product product = dataGenerator.generateProduct(true);
        product.setId(7L);
        final ShoppingCartItem model = dataGenerator.generateShoppingCartItem(product, "user");
        model.setId(9L);
        Integer currentQuantity = model.getQuantity();

        when(productService.findProductById(anyLong()))
                .thenReturn(Optional.of(product));
        when(shoppingCartItemRepository.findByProductIdAndUsername(7L, "user"))
                .thenReturn(Optional.of(model));

        final Optional<ShoppingCartItemDto> shoppingCartItemDto
                = shoppingCartItemService.addOneItemOfProduct(7L);

        assertNotNull("The result should not be null", shoppingCartItemDto);
        assertTrue("The result should not be empty", shoppingCartItemDto.isPresent());
        assertEquals(shoppingCartItemDto.get().getProduct().getId(), product.getId());
        assertThat(
                shoppingCartItemDto.get().getQuantity(),
                equalTo(currentQuantity + 1)
        );
    }

    @Test
    public void shouldNotAddShoppingCartItemWhenInvalidProductIdIsSpecified() {
        when(productService.findProductById(anyLong()))
                .thenReturn(Optional.empty());

        final Optional<ShoppingCartItemDto> shoppingCartItemDto
                = shoppingCartItemService.addOneItemOfProduct(7L);

        assertNotNull("The result should not be null", shoppingCartItemDto);
        assertFalse("The result should be empty", shoppingCartItemDto.isPresent());
    }

    @Test
    public void shouldConvertModelToShoppingCartDto() {
        final Product product = dataGenerator.generateProduct(true);
        product.setId(7L);
        final ShoppingCartItem model = dataGenerator.generateShoppingCartItem(
                product, "user"
        );
        model.setId(9L);

        final ShoppingCartItemDto dto =
                shoppingCartItemService.convertToShoppingCartDto(model);

        assertNotNull("The DTO should not be null", dto);
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getQuantity(), dto.getQuantity());
        assertEquals(model.getProduct().getId(), dto.getProduct().getId());
        assertEquals(model.getProduct().getName(), dto.getProduct().getName());
        assertEquals(model.getCreatedDate(), dto.getCreatedDate());
        assertEquals(model.getLastModifiedDate(), dto.getLastModifiedDate());
    }

    @Test
    public void shouldConvertFromShoppingCartDtoToModel() {
        final ShoppingCartItemDto dto = new ShoppingCartItemDto();
        dto.setId(7L);
        dto.setProduct(dataGenerator.generateProductDto(true));
        dto.setQuantity(2);
        dto.setCreatedDate(new Date());
        dto.setLastModifiedDate(new Date());

        final ShoppingCartItem model =
                shoppingCartItemService.convertFromShoppingCartDto(dto);

        assertNotNull("The model should not be null", model);
        assertEquals(dto.getId(), model.getId());
        assertEquals(dto.getQuantity(), model.getQuantity());
        assertEquals(dto.getProduct().getId(), model.getProduct().getId());
        assertEquals(dto.getProduct().getName(), model.getProduct().getName());
        assertEquals(dto.getCreatedDate(), model.getCreatedDate());
        assertEquals(dto.getLastModifiedDate(), model.getLastModifiedDate());
    }

    @Test
    public void givenOnlyOneItemIsLeft_whenRemoveOneItemOfProduct_thenRemovesShoppingCartItemForCurrentUser() {
        final Product existingProduct = dataGenerator.generateProduct(true);
        existingProduct.setId(7L);
        final ShoppingCartItem existingShoppingCartItem =
                dataGenerator.generateShoppingCartItem(existingProduct, "user");
        existingShoppingCartItem.setQuantity(1);

        when(productService.findProductById(7L)).thenReturn(Optional.of(existingProduct));
        when(shoppingCartItemRepository.findByProductIdAndUsername(7L, "user"))
                .thenReturn(Optional.of(existingShoppingCartItem));

        final Optional<ShoppingCartItemDto> newShoppingCartItemDto =
                shoppingCartItemService.removeOneItemOfProduct(existingProduct.getId());

        verify(shoppingCartItemRepository, times(1))
                .delete(existingShoppingCartItem);

        assertNotNull(newShoppingCartItemDto);
        assertTrue(newShoppingCartItemDto.isPresent());
        assertThat(newShoppingCartItemDto.get().getQuantity(), equalTo(0));
    }

    @Test
    @WithMockUser(username = "user@nexio.com")
    public void shouldReturnActiveUser() {
        assertEquals(
                "Expected to return username of active user",
                "user@nexio.com",
                shoppingCartItemService.currentUsername()
        );
    }
}