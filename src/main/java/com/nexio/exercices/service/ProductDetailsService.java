package com.nexio.exercices.service;

import com.nexio.exercices.dto.ProductDetailsDto;
import com.nexio.exercices.model.ProductDetails;
import com.nexio.exercices.persistence.ProductDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;
    private final ModelMapper modelMapper;

    public ProductDetailsService(ProductDetailsRepository productDetailsRepository,
                                 ModelMapper modelMapper) {
        this.productDetailsRepository = productDetailsRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<ProductDetailsDto> getProductDetails(Long productId) {
        return productDetailsRepository.findByProductId(productId)
                .map(this::convertToProductDetailsDto);
    }

    protected ProductDetailsDto convertToProductDetailsDto(ProductDetails productDetails) {
        return modelMapper.map(productDetails, ProductDetailsDto.class);
    }
}
