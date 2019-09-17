package com.nexio.excercices.persistence;

import com.nexio.excercices.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
