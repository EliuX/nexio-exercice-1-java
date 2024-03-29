package com.nexio.exercices.controller;

import com.nexio.exercices.constant.Roles;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductDetailsRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = Roles.USER)
public class ProductDetailsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductDetailsRepository productDetailsRepository;

    @Autowired
    private DataGenerator dataGenerator;

    @Test
    public void givenProductDetails_whenGetDetailsOfProduct_thenReturnJson() throws Exception {
        final Product existingProduct = dataGenerator.generateProductWithDetails(true);
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
}