package com.nexio.exercices.persistence;

import com.nexio.exercices.model.ShoppingCartItem;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class JPASpecifications {

    public static <T> Specification<T> belongsToActiveUser() {
        return (Specification<T>) (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("username"), currentUsername());
    }

    private static String currentUsername() {
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
