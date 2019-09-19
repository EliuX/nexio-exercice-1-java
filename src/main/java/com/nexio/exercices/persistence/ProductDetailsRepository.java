package com.nexio.exercices.persistence;

import com.nexio.exercices.model.ProductDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductDetailsRepository extends CrudRepository<ProductDetails, Long> {

    Optional<ProductDetails> findByProductId(Long productId);
}
