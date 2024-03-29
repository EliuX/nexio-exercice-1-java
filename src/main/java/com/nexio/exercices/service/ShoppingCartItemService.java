package com.nexio.exercices.service;

import com.nexio.exercices.dto.ShoppingCartDto;
import com.nexio.exercices.dto.ShoppingCartItemDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
import com.nexio.exercices.persistence.ShoppingCartItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nexio.exercices.persistence.JPASpecifications.belongsToActiveUser;
import static org.springframework.data.domain.Sort.Order.desc;

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

    public ShoppingCartDto getContent() {
        ShoppingCartDto content = new ShoppingCartDto();
        content.setItems(getAllItems());
        return content;
    }

    public List<ShoppingCartItemDto> getAllItems() {
        final List<ShoppingCartItem> allItemsForCurrentUser =
                shoppingCartItemRepository.findAll(
                        belongsToActiveUser(),
                        Sort.by(desc("lastModifiedDate"))
                );
        return allItemsForCurrentUser.stream()
                .map(this::convertToShoppingCartDto)
                .collect(Collectors.toList());
    }

    protected ShoppingCartItemDto convertToShoppingCartDto(ShoppingCartItem model) {
        return modelMapper.map(model, ShoppingCartItemDto.class);
    }

    protected ShoppingCartItem convertFromShoppingCartDto(ShoppingCartItemDto dto) {
        return modelMapper.map(dto, ShoppingCartItem.class);
    }

    private ShoppingCartItem incrementShoppingCartQuantityForProduct(Product product) {
        final ShoppingCartItem currentShoppingCartItem =
                shoppingCartItemRepository.findByProductIdAndCurrentUser(
                        product.getId()
                ).orElseGet(() -> new ShoppingCartItem(
                        product, 0
                ));

        return saveUpdatedShoppingCartItem(
                currentShoppingCartItem.incrementQuantityAndGet()
        );
    }

    private Optional<ShoppingCartItem> decreaseShoppingCartQuantityForProduct(
            Long productId
    ) {
        return shoppingCartItemRepository.findByProductIdAndCurrentUser(
                productId
        )
                .map(ShoppingCartItem::decreaseQuantityAndGet)
                .map(this::saveUpdatedShoppingCartItem);
    }

    private ShoppingCartItem saveUpdatedShoppingCartItem(ShoppingCartItem item) {
        if (item.isEmpty()) {
            shoppingCartItemRepository.delete(item);
            return item;
        } else {
            return shoppingCartItemRepository.save(item);
        }
    }
}
