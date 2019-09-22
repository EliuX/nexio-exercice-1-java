package com.nexio.exercices.persistence;

import com.nexio.exercices.model.ShoppingCartItem;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public interface ShoppingCartItemRepository extends CrudRepository<ShoppingCartItem, Long> {
    Optional<ShoppingCartItem> findByProductIdAndUsername(Long product, String username);

    List<ShoppingCartItem> findAllByUsername(String username);
}
