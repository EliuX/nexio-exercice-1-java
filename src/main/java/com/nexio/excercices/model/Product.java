package com.nexio.excercices.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 1, max = 100)
    private String name;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    private ProductDetails productDetails;

    Product() {
        this("Pas de produit", BigDecimal.ZERO, ProductDetails.NULL_PRODUCT_DETAILS);
    }

    public Product(String name, BigDecimal price, ProductDetails productDetails) {
        this.name = name;
        this.price = price;
        this.productDetails = productDetails;
        this.productDetails.setProduct(this);
    }

    public Long getId() {
        return id;
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
        this.productDetails = productDetails;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f CAD)", getName(), getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getName(), product.getName()) &&
                Objects.equals(getPrice(), product.getPrice()) &&
                Objects.equals(getProductDetails(), product.getProductDetails());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getProductDetails());
    }
}