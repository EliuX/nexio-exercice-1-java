package com.nexio.exercices.service;

import com.nexio.exercices.dto.ProductDto;
import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository,
                          ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public List<ProductDto> getProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToProductDto)
                .collect(Collectors.toList());
    }

    protected ProductDto convertToProductDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
