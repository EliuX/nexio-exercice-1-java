package com.nexio.exercices.controller;

import com.nexio.exercices.dto.ShoppingCartDto;
import com.nexio.exercices.dto.ShoppingCartItemChangeDto;
import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.exception.NotFoundException;
import com.nexio.exercices.service.ShoppingCartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartItemService shoppingCartItemService;

    @GetMapping
    public ShoppingCartDto getContent() {
        return shoppingCartItemService.getContent();
    }

    @PutMapping("/items")
    public ResponseEntity<ShoppingCartItemDto> addNewItemOfProduct(
            @Valid @RequestBody ShoppingCartItemChangeDto newItemRequestBody
    ) {
        final ShoppingCartItemDto appliedChangeDto =
                shoppingCartItemService.addOneItemOfProduct(
                        newItemRequestBody.getProductId()
                ).orElseThrow(() -> new NotFoundException("Produit introuvable"));

        return new ResponseEntity<>(appliedChangeDto,
                appliedChangeDto.isNew() ? HttpStatus.CREATED : HttpStatus.OK);
    }

    @GetMapping("/items")
    public List<ShoppingCartItemDto> getItems() {
        return shoppingCartItemService.getAllItems();
    }

    @DeleteMapping("/items")
    public ShoppingCartItemDto removeItemOfProduct(
            @Valid @RequestBody ShoppingCartItemChangeDto newItemRequestBody
    ) {
        return shoppingCartItemService.removeOneItemOfProduct(
                newItemRequestBody.getProductId()
        ).orElseThrow(() -> new NotFoundException(
                "Le produit specifié n’est pas dans le panier"
        ));
    }
}
