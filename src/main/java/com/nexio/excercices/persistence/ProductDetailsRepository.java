package com.nexio.excercices.persistence;

import com.nexio.excercices.model.ProductDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductDetailsRepository extends CrudRepository<ProductDetails, Long> {

    Optional<ProductDetails> findByProductId(Long productId);
}
