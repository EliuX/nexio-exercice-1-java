package com.nexio.exercices.persistence;

import com.nexio.exercices.model.ShoppingCartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartItemRepository extends CrudRepository<ShoppingCartItem, Long> {
    Optional<ShoppingCartItem> findByProductId(Long product);

    List<ShoppingCartItem> findAll();
}
