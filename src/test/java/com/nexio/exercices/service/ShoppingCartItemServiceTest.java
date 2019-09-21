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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCartItemServiceTest {

    @Autowired
    private ShoppingCartItemService shoppingCartItemService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ShoppingCartItemRepository shoppingCartItemRepository;

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
    public void shouldCreateNewShoppingCartItem() {
        final Product product = DataGenerator.generateProduct(true);
        product.setId(7L);

        when(productService.findProductById(anyLong()))
                .thenReturn(Optional.of(product));
        when(shoppingCartItemRepository.findByProductId(7L))
                .thenReturn(Optional.empty());

        final Optional<ShoppingCartItemDto> shoppingCartItemDto
                = shoppingCartItemService.addOneItemOfProduct(7L);

        assertNotNull("The result should not be null", shoppingCartItemDto);
        assertTrue("The result should not be empty", shoppingCartItemDto.isPresent());
        assertEquals(shoppingCartItemDto.get().getProductId(), product.getId());
        assertThat(shoppingCartItemDto.get().getQuantity(), equalTo(1));
    }

    @Test
    public void shouldIncrementQuantifyOfExistingShoppingCartItem() {
        final Product product = DataGenerator.generateProduct(true);
        product.setId(7L);
        final ShoppingCartItem model = DataGenerator.generateShoppingCartItem(product);
        model.setId(9L);
        Integer currentQuantity = model.getQuantity();

        when(productService.findProductById(anyLong()))
                .thenReturn(Optional.of(product));
        when(shoppingCartItemRepository.findByProductId(7L))
                .thenReturn(Optional.of(model));

        final Optional<ShoppingCartItemDto> shoppingCartItemDto
                = shoppingCartItemService.addOneItemOfProduct(7L);

        assertNotNull("The result should not be null", shoppingCartItemDto);
        assertTrue("The result should not be empty", shoppingCartItemDto.isPresent());
        assertEquals(shoppingCartItemDto.get().getProductId(), product.getId());
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
        final Product product = DataGenerator.generateProduct(true);
        product.setId(7L);
        final ShoppingCartItem model = DataGenerator.generateShoppingCartItem(product);
        model.setId(9L);

        final ShoppingCartItemDto dto =
                shoppingCartItemService.convertToShoppingCartDto(model);

        assertNotNull("The DTO should not be null", dto);
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getQuantity(), dto.getQuantity());
        assertEquals(model.getProduct().getId(), dto.getProductId());
        assertEquals(model.getProduct().getName(), dto.getProductName());
    }

    @Test
    public void shouldConvertFromShoppingCartDtoToModel() {
        final ShoppingCartItemDto dto = new ShoppingCartItemDto();
        dto.setId(7L);
        dto.setProductId(9L);
        dto.setQuantity(2);

        final ShoppingCartItem model =
                shoppingCartItemService.convertFromShoppingCartDto(dto);

        assertNotNull("The model should not be null", model);
        assertEquals(dto.getId(), model.getId());
        assertEquals(dto.getQuantity(), model.getQuantity());
        assertEquals(dto.getProductId(), model.getProduct().getId());
        assertEquals(dto.getProductName(), model.getProduct().getName());
    }

    @Test
    public void givenOnlyOneItemIsLeft_whenRemoveOneItemOfProduct_thenRemovesShoppingCartItem() {
        final Product existingProduct = DataGenerator.generateProduct(true);
        existingProduct.setId(7L);
        final ShoppingCartItem existingShoppingCartItem =
                DataGenerator.generateShoppingCartItem(existingProduct);
        existingShoppingCartItem.setQuantity(1);

        when(productService.findProductById(7L)).thenReturn(Optional.of(existingProduct));
        when(shoppingCartItemRepository.findByProductId(7L)).thenReturn(Optional.of(existingShoppingCartItem));

        final Optional<ShoppingCartItemDto> newShoppingCartItemDto =
                shoppingCartItemService.removeOneItemOfProduct(existingProduct.getId());

        verify(shoppingCartItemRepository, times(1)).delete(existingShoppingCartItem);

        assertNotNull(newShoppingCartItemDto);
        assertTrue(newShoppingCartItemDto.isPresent());
        assertThat(newShoppingCartItemDto.get().getQuantity(), equalTo(0));
    }
}