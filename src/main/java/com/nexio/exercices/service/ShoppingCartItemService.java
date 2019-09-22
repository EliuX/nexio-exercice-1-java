package com.nexio.exercices.service;

import com.nexio.exercices.dto.ShoppingCartDto;
import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
import com.nexio.exercices.persistence.ShoppingCartItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartItemService {

    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public ShoppingCartItemService(ShoppingCartItemRepository shoppingCartItemRepository,
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

    public Optional<ShoppingCartItemDto> removeOneItemOfProduct(Long productId) {
        return productService.findProductById(productId)
                .map(Product::getId)
                .flatMap(this::decreaseShoppingCartQuantityForProduct)
                .map(this::convertToShoppingCartDto);
    }

    public List<ShoppingCartItemDto> getAllItems() {
        return shoppingCartItemRepository.findAllByUsername(currentUsername()).stream()
                .map(this::convertToShoppingCartDto)
                .collect(Collectors.toList());
    }

    public ShoppingCartDto getContent() {
        ShoppingCartDto content = new ShoppingCartDto();
        content.setItems(getAllItems());
        return content;
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
        final ShoppingCartItem currentShoppingCartItem =
                shoppingCartItemRepository.findByProductIdAndUsername(
                        product.getId(), currentUsername()
                ).orElseGet(() -> new ShoppingCartItem(
                        product, 0, currentUsername()
                ));

        return saveUpdatedShoppingCartItem(
                currentShoppingCartItem.incrementQuantityAndGet()
        );
    }

    private Optional<ShoppingCartItem> decreaseShoppingCartQuantityForProduct(
            Long productId
    ) {
        return shoppingCartItemRepository.findByProductIdAndUsername(
                productId, currentUsername()
        )
                .map(ShoppingCartItem::decreaseQuantityAndGet)
                .map(this::saveUpdatedShoppingCartItem);
    }

    private ShoppingCartItem saveUpdatedShoppingCartItem(ShoppingCartItem item) {
        if (item.isEmpty()) {
            // TODO Filter by username
            shoppingCartItemRepository.delete(item);
            return item;
        } else {
            return saveShoppingCartItemForCurrentUser(item);
        }
    }

    private ShoppingCartItem saveShoppingCartItemForCurrentUser(ShoppingCartItem item) {
        if (item.getUsername() == null) {
            item.setUsername(currentUsername());
        }
        return shoppingCartItemRepository.save(item);
    }

    String currentUsername() {
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
