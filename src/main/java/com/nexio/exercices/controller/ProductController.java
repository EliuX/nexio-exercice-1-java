package com.nexio.exercices.controller;

import com.nexio.exercices.dto.ProductDto;
import com.nexio.exercices.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> getProductsCatalog() {
        return productService.getProducts();
    }
}
