package com.nexio.exercices.model;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "shopping_cart_item")
public class ShoppingCartItem {
    @Positive
    Integer quantity;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        if (quantity == null) {
            quantity = 0;
        }

        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format(
                "ShopingCart item {product: %s, quantity: %d}",
                getProduct(),
                getQuantity()
        );
    }

    public ShoppingCartItem incrementQuantityAndGet() {
        setQuantity(getQuantity() + 1);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return Objects.equals(getQuantity(), that.getQuantity()) &&
                Objects.equals(getProduct(), that.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantity(), getProduct());
    }
}
