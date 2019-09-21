package com.nexio.exercices.service;

import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
import com.nexio.exercices.persistence.ShoppingCartItemRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCartServiceTest {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Test
    public void shouldCreateNewShoppingCartItem() {
        final Product product = DataGenerator.generateProduct(true);
        product.setId(7L);

        when(productService.findProductById(anyLong()))
                .thenReturn(Optional.of(product));
        when(shoppingCartItemRepository.findByProductId(7L))
                .thenReturn(Optional.empty());

        final Optional<ShoppingCartItemDto> shoppingCartItemDto
                = shoppingCartService.addOneItemOfProduct(7L);

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
                = shoppingCartService.addOneItemOfProduct(7L);

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
                = shoppingCartService.addOneItemOfProduct(7L);

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
                shoppingCartService.convertToShoppingCartDto(model);

        assertNotNull("The DTO should not be null", dto);
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getQuantity(), dto.getQuantity());
        assertEquals(model.getProduct().getId(), dto.getProductId());
    }

    @Test
    public void shouldConvertFromShoppingCartDtoToModel() {
        final ShoppingCartItemDto dto = new ShoppingCartItemDto();
        dto.setId(7L);
        dto.setProductId(9L);
        dto.setQuantity(2);

        final ShoppingCartItem model =
                shoppingCartService.convertFromShoppingCartDto(dto);

        assertNotNull("The model should not be null", model);
        assertEquals(dto.getId(), model.getId());
        assertEquals(dto.getQuantity(), model.getQuantity());
        assertEquals(dto.getProductId(), model.getProduct().getId());
    }
}