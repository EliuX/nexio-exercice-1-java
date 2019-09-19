package com.nexio.exercices.persistence;

import com.nexio.exercices.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
