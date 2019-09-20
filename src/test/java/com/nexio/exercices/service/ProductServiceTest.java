package com.nexio.exercices.service;

import com.nexio.exercices.dto.ProductDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @Test
    public void shouldReturnListOfProducts() {
        final int COUNT_OF_EXISTING_PRODUCTS = 5;
        final List<Product> products = Stream.generate(
                () -> DataGenerator.generateProductWithDetails(false)
        )
                .limit(COUNT_OF_EXISTING_PRODUCTS)
                .collect(Collectors.toList());
        Mockito.when(productRepository.findAll()).thenReturn(products);

        final List<ProductDto> productsDtoList = productService.getProducts();

        assertNotNull("The result should not be null", productsDtoList);
        assertFalse("The result should not empty", productsDtoList.isEmpty());
        assertThat(
                "The result should have the expected size",
                productsDtoList.size(),
                equalTo(COUNT_OF_EXISTING_PRODUCTS)
        );
    }

    @Test
    public void givenExistingProduct_whenFindByProductId_thenReturnProduct() {
        final Product product = DataGenerator.generateProduct(true);

        Mockito.when(productRepository.findById(anyLong()))
                .thenReturn(Optional.of(product));

        final Optional<Product> foundItem = productService.findProductById(1L);

        assertTrue("The result should not be null", foundItem.isPresent());
    }

    @Test
    public void shouldConvertProductModelToDto() {
        final Product product = DataGenerator.generateProduct(true);

        final ProductDto productDto = productService.convertToProductDto(product);

        assertNotNull("The DTO should not be null", productDto);
        assertEquals(product.getId(), productDto.getId());
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getPrice(), productDto.getPrice());
    }
}