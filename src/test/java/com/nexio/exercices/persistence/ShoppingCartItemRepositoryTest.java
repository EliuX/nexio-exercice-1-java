package com.nexio.exercices.persistence;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ShoppingCartItem;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShoppingCartItemRepositoryTest {

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void shouldSaveShoppingCart() {
        final Product product = DataGenerator.generateProduct(true);
        final ShoppingCartItem shoppingCartItem =
                DataGenerator.generateShoppingCartItem(product);

        final ShoppingCartItem savedShoppingCart =
                shoppingCartItemRepository.save(shoppingCartItem);

        ShoppingCartItem foundShoppingCartItem =
                entityManager.find(ShoppingCartItem.class, savedShoppingCart.getId());

        assertNotNull(
                "A shopping cart item was found",
                foundShoppingCartItem
        );

        assertThat(foundShoppingCartItem, equalTo(savedShoppingCart));
    }

    @Test
    public void shouldGetExistingShoppingCart() {
        final Product product = DataGenerator.generateProduct(false);
        final ShoppingCartItem shoppingCartItem =
                DataGenerator.generateShoppingCartItem(product);

        final ShoppingCartItem savedItem =
                entityManager.persist(shoppingCartItem);

        final Optional<ShoppingCartItem> foundItem =
                shoppingCartItemRepository.findById(savedItem.getId());

        assertTrue(
                "A shopping cart item was found",
                foundItem.isPresent()
        );

        assertThat(foundItem.get(), equalTo(savedItem));
    }

    @Test
    public void shouldDeleteByIdExistingElement() {
        final Product product = DataGenerator.generateProduct(false);
        final ShoppingCartItem shoppingCartItem =
                DataGenerator.generateShoppingCartItem(product);

        final Long id = (Long) entityManager.persistAndGetId(shoppingCartItem);

        shoppingCartItemRepository.deleteById(id);

        final ShoppingCartItem foundElement =
                entityManager.find(ShoppingCartItem.class, id);

        assertNull(
                "The deleted element should not be found",
                foundElement
        );
    }

    @Test
    public void shouldShoppingCartItemByValidProductId() {
        final Product product = DataGenerator.generateProduct(false);
        final ShoppingCartItem shoppingCartItem =
                DataGenerator.generateShoppingCartItem(product);

        final Long productId =
                (Long) entityManager.persistAndGetId(product);
        final Long shoppingCartItemId =
                (Long) entityManager.persistAndGetId(shoppingCartItem);

        final Optional<ShoppingCartItem> foundItem =
                shoppingCartItemRepository.findByProductId(productId);

        assertTrue("Was able to find an item", foundItem.isPresent());
        assertEquals(foundItem.get().getId(), shoppingCartItemId);
    }


    @After
    public void clearData() {
        entityManager.clear();
    }
}