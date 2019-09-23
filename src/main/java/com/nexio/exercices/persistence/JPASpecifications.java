package com.nexio.exercices.persistence;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class JPASpecifications {

    public static <T> Specification<T> belongsToActiveUser() {
        return belongsToUser(currentUsername());
    }

    public static <T> Specification<T> belongsToUser(String username) {
        return (Specification<T>) (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("username"), username);
    }

    static String currentUsername() {
        final UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }
}
