package com.nexio.exercices.controller;

import com.nexio.exercices.exception.ProductNotFound;
import com.nexio.exercices.model.ProductDetails;
import com.nexio.exercices.persistence.ProductDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/products/{productId}/details")
public class ProductDetailsController {

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @GetMapping
    public ModelAndView showDetailsOfProduct(@PathVariable Long productId) {
        final ModelAndView modelAndView = new ModelAndView("products/details/show");

        final ProductDetails productDetails = productDetailsRepository.findByProductId(productId)
                .orElseThrow(ProductNotFound::new);
        modelAndView.addObject("product", productDetails.getProduct());
        modelAndView.addObject("details", productDetails);

        return modelAndView;
    }
}
