package com.nexio.exercices.controller;

import com.nexio.exercices.dto.ShoppingCartItemChangeDto;
import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.exception.NotFoundException;
import com.nexio.exercices.service.ShoppingCartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-cart/items")
public class ShoppingCartItemController {

    @Autowired
    private ShoppingCartItemService shoppingCartItemService;

    @PutMapping
    public ResponseEntity<ShoppingCartItemDto> addNewItemOfProduct(
            @RequestBody ShoppingCartItemChangeDto newItemRequestBody
    ) {
        final ShoppingCartItemDto appliedChangeDto = shoppingCartItemService.addOneItemOfProduct(
                newItemRequestBody.getProductId()
        ).orElseThrow(()-> new NotFoundException("Product not found"));

        return new ResponseEntity<>(appliedChangeDto,
                appliedChangeDto.isNew() ? HttpStatus.CREATED : HttpStatus.OK);
    }

    @GetMapping
    public List<ShoppingCartItemDto> getItems() {
        return shoppingCartItemService.getAllItems();
    }

    @DeleteMapping
    public ShoppingCartItemDto removeItemOfProduct(
            @RequestBody ShoppingCartItemChangeDto newItemRequestBody
    ) {
        return shoppingCartItemService.removeOneItemOfProduct(
                newItemRequestBody.getProductId()
        ).orElseThrow(()-> new NotFoundException(
                "There is no item in the shopping cart for the specified product"
        ));
    }
}
