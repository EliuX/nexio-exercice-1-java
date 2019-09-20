package com.nexio.exercices.persistence;

import com.nexio.exercices.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    List<Product> findAll();
}
