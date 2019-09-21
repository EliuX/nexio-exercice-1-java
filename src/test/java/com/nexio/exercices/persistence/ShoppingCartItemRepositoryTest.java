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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    private DataGenerator dataGenerator = new DataGenerator();

    @Test
    public void shouldSaveShoppingCart() {
        final Product product = dataGenerator.generateProduct(true);
        final ShoppingCartItem shoppingCartItem =
                dataGenerator.generateShoppingCartItem(product);

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
    public void shouldFindShoppingCartItemByValidProductId() {
        final ShoppingCartItem shoppingCartItem = createNewShoppingCartItem();

        final Long productId = (Long) entityManager.persistAndGetId(
                shoppingCartItem.getProduct()
        );
        final Long shoppingCartItemId = (Long) entityManager.persistAndGetId(
                shoppingCartItem
        );

        final Optional<ShoppingCartItem> foundItem =
                shoppingCartItemRepository.findByProductId(productId);

        assertTrue("Was able to find an item", foundItem.isPresent());
        assertEquals(foundItem.get().getId(), shoppingCartItemId);
    }

    @Test
    public void shouldGetAllShoppingCartItems() {
        final int COUNT_OF_EXISTING_ITEMS = 7;
        Stream.generate(this::createNewShoppingCartItem)
                .limit(COUNT_OF_EXISTING_ITEMS)
                .forEach(item -> {
                    entityManager.persist(item.getProduct());
                    entityManager.persist(item);
                });

        final List<ShoppingCartItem> allItems =
                shoppingCartItemRepository.findAll();

        assertNotNull("The result should not be null", allItems);
        assertFalse("The result should not be empty", allItems.isEmpty());
        assertEquals(COUNT_OF_EXISTING_ITEMS, allItems.size());
    }

    private ShoppingCartItem createNewShoppingCartItem() {
        final Product product = dataGenerator.generateProduct(false);
        return dataGenerator.generateShoppingCartItem(product);
    }

    @After
    public void clearData() {
        entityManager.clear();
    }
}