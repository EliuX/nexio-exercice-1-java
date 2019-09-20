package com.nexio.exercices.persistence;

import com.nexio.exercices.model.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {

    Optional<ProductDetails> findByProductId(Long productId);
}
