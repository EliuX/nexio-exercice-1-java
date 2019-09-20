package com.nexio.exercices.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, max = 100)
    private String name;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private ProductDetails productDetails;

    Product() {
        this("Pas de produit", BigDecimal.ZERO);
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.productDetails = ProductDetails.NULL_PRODUCT_DETAILS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductDetails getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ProductDetails productDetails) {
        if (productDetails != null) {
            productDetails.setProduct(this);
        }

        this.productDetails = productDetails;
    }

    @Override
    public String toString() {
        return String.format("Product {name: %s, price: %.2f CAD}", getName(), getPrice());
    }
}
