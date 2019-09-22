package com.nexio.exercices.controller;

import com.nexio.exercices.dto.ProductDetailsDto;
import com.nexio.exercices.exception.NotFoundException;
import com.nexio.exercices.service.ProductDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products/{productId}/details")
public class ProductDetailsController {

    @Autowired
    private ProductDetailsService productDetailsService;

    @GetMapping
    public ProductDetailsDto getDetailsOfProduct(@PathVariable Long productId) {
        return productDetailsService.getProductDetails(productId)
                .orElseThrow(() -> new NotFoundException("Produit introuvable"));
    }
}
