package com.nexio.exercices.service;

import com.nexio.exercices.dto.ProductDetailsDto;
import com.nexio.exercices.dto.ProductDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ProductDetails;
import com.nexio.exercices.persistence.ProductDetailsRepository;
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
public class ProductDetailsServiceTest {

    @Autowired
    ProductDetailsService productDetailsService;

    @MockBean
    ProductDetailsRepository productDetailsRepository;
    
    @Autowired
    private DataGenerator dataGenerator;

    @Test
    public void shouldNotReturnProductDetails() {
        Mockito.when(productDetailsRepository.findByProductId(anyLong()))
                .thenReturn(Optional.empty());

        final Optional<ProductDetailsDto> productDetails =
                productDetailsService.getProductDetails(1L);

        assertFalse(
                "There should be no product details",
                productDetails.isPresent()
        );
    }

    @Test
    public void shouldReturnExistingProductDetails() {
        final Product product = dataGenerator.generateProductWithDetails(false);
        Mockito.when(productDetailsRepository.findByProductId(anyLong()))
                .thenReturn(Optional.of(product.getProductDetails()));

        final Optional<ProductDetailsDto> productDetails =
                productDetailsService.getProductDetails(1L);

        assertTrue(
                "The result should have details of the product",
                productDetails.isPresent()
        );
    }

    @Test
    public void shouldConvertProductDetailsModelToDto() {
        final Product product = dataGenerator.generateProductWithDetails(true);

        final ProductDetailsDto productDetailsDto =
                productDetailsService.convertToProductDetailsDto(product.getProductDetails());


        assertNotNull("The DTO should not be null", productDetailsDto);
        assertEquals(product.getProductDetails().getId(), productDetailsDto.getId());
        assertEquals(product.getProductDetails().getEdible(), productDetailsDto.getEdible());
        assertEquals(product.getProductDetails().getDescription(), productDetailsDto.getDescription());
    }
}