package com.nexio.exercices.controller;

import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.exception.NotFoundException;
import com.nexio.exercices.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PutMapping("/add/{productId}")
    public ShoppingCartItemDto addOneItemToShoppingCartOfProduct(@PathVariable Long productId) {
        return shoppingCartService.addProduct(productId)
                .orElseThrow(NotFoundException::new);
    }
}
