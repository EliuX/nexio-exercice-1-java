package com.nexio.exercices.controller;

import com.nexio.exercices.dto.NewShoppingCartItemDto;
import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.exception.NotFoundException;
import com.nexio.exercices.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-cart/items")
public class ShoppingCartItemController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PutMapping
    public ResponseEntity<ShoppingCartItemDto> addOneItemToShoppingCartOfProduct(
            @RequestBody NewShoppingCartItemDto newItemRequestBody
    ) {
        final ShoppingCartItemDto appliedChangeDto = shoppingCartService.addOneItemOfProduct(
                newItemRequestBody.getProductId()
        ).orElseThrow(NotFoundException::new);

        return new ResponseEntity<>(appliedChangeDto,
                appliedChangeDto.isNew() ? HttpStatus.CREATED : HttpStatus.OK);
    }
}
