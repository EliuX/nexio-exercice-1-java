package com.nexio.exercices.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "product_details")
public class ProductDetails {

    public static final ProductDetails NULL_PRODUCT_DETAILS = new ProductDetails();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "productDetails")
    private Product product;

    @NotEmpty(message = "La description d’un produit ne peut pas être blanche")
    @Size(min = 3, max = 500)
    private String description;

    @NotNull
    private Boolean edible;

    public ProductDetails() {
        this("Pas des détails", false);
    }

    public ProductDetails(String description, Boolean edible) {
        this.description = description;
        this.edible = edible;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEdible() {
        return edible;
    }

    public void setEdible(Boolean edible) {
        this.edible = edible;
    }

    @Override
    public String toString() {
        return String.format(
                "Product Details {product:%s, edible:%s, description:%s}",
                product == null ? "<none>" : product.getName(),
                edible.toString(),
                description == null || description.isEmpty()
                        ? ""
                        : description.substring(0, Math.min(3, description.length()))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetails that = (ProductDetails) o;
        return Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getEdible(), that.getEdible());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getEdible());
    }
}
