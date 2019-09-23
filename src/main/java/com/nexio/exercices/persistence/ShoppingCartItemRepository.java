package com.nexio.exercices.persistence;

import com.nexio.exercices.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem, Long>
        , JpaSpecificationExecutor {

    @Query("select i from #{#entityName} i where i.product.id = ?1 " +
            "and i.username = ?#{ principal?.username }")
    Optional<ShoppingCartItem> findByProductIdAndCurrentUser(Long productId);
}
