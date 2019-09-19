package com.nexio.exercices.controller;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ModelAndView showProductCatalogs() {
        ModelAndView response = new ModelAndView("products/list");

        final Iterable<Product> products = productRepository.findAll();
        response.addObject("products", products);

        return response;
    }
}
