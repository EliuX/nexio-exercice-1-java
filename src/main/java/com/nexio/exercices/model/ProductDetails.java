package com.nexio.exercices.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product_details")
public class ProductDetails {

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
}
