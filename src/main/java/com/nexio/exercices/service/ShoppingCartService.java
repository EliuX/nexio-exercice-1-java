package com.nexio.exercices.service;

import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
import com.nexio.exercices.persistence.ShoppingCartItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ShoppingCartService(ShoppingCartItemRepository shoppingCartItemRepository,
                               ProductService productService,
                               ModelMapper modelMapper) {
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    public Optional<ShoppingCartItemDto> addOneItemOfProduct(Long productId) {
        return productService.findProductById(productId)
                .map(this::incrementShoppingCartQuantityForProduct)
                .map(this::convertToShoppingCartDto);
    }

    protected ShoppingCartItemDto convertToShoppingCartDto(ShoppingCartItem model) {
        return modelMapper.map(model, ShoppingCartItemDto.class);
    }

    protected ShoppingCartItem convertFromShoppingCartDto(ShoppingCartItemDto dto) {
        return modelMapper.map(dto, ShoppingCartItem.class);
    }

    private ShoppingCartItem incrementShoppingCartQuantityForProduct(
            Product product
    ) {
        final ShoppingCartItem currentShoppingCart =
                shoppingCartItemRepository.findByProductId(product.getId())
                        .orElseGet(() -> new ShoppingCartItem(product, 0));

        return currentShoppingCart.incrementQuantityAndGet();
    }
}
