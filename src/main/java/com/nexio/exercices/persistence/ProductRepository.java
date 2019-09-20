package com.nexio.exercices.persistence;

import com.nexio.exercices.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    @Override
    List<Product> findAll();
}
