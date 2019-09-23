package com.nexio.exercices.persistence;

import com.nexio.exercices.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
