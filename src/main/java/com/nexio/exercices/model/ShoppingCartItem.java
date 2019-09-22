package com.nexio.exercices.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull

    private String username;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(Product product, Integer quantity, String username) {
        this.product = product;
        this.quantity = quantity;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return String.format(
                "ShoppingCart item {product: %s, quantity: %d}",
                getProduct(),
                getQuantity()
        );
    }

    public ShoppingCartItem incrementQuantityAndGet() {
        setQuantity(getQuantity() + 1);
        return this;
    }

    public ShoppingCartItem decreaseQuantityAndGet() {
        setQuantity(Math.max(0, getQuantity() - 1));
        return this;
    }

    @Transient
    public boolean isEmpty() {
        return getQuantity() < 1;
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
