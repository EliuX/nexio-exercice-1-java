package com.nexio.exercices.controller;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductDetailsRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.After;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductDetailsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductDetailsRepository productDetailsRepository;

    @Test
    public void givenProductDetails_whenGetDetailsOfProduct_thenReturnJson() throws Exception {
        final Product existingProduct = DataGenerator.generateProductWithDetails(true);
        when(productDetailsRepository.findByProductId(anyLong()))
                .thenReturn(Optional.of(existingProduct.getProductDetails()));

        mvc.perform(get("/products/1/details")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.edible", is(true)))
                .andExpect(jsonPath(
                        "$.description",
                        is(existingProduct.getProductDetails().getDescription())
                ));
    }

    @Test
    public void givenNoProductDetails_whenGetDetailsOfProduct_thenNotFoundStatusWithEmptyJson()
            throws Exception {
        when(productDetailsRepository.findByProductId(anyLong()))
                .thenReturn(Optional.empty());

        mvc.perform(get("/products/1/details")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @After
    public void resetMockBean() {
        reset(productDetailsRepository);
    }
}