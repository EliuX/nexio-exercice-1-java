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
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@WithMockUser(username = "user")
public class ShoppingCartItemRepositoryTest {

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    private DataGenerator dataGenerator = new DataGenerator();

    @Test
    public void shouldSaveShoppingCartOfAUser() {
        final Product product = dataGenerator.generateProduct(true);
        final ShoppingCartItem shoppingCartItem =
                dataGenerator.generateShoppingCartItem(product, "user");

        entityManager.persist(product);
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
    public void shouldFindShoppingCartItemByValidProductIdAndUsername() {
        final ShoppingCartItem shoppingCartItem = createNewShoppingCartItem();

        final Long productId = (Long) entityManager.persistAndGetId(
                shoppingCartItem.getProduct()
        );
        final ShoppingCartItem savedItem = shoppingCartItemRepository.save(shoppingCartItem);

        final Optional<ShoppingCartItem> foundItem =
                shoppingCartItemRepository.findByProductIdAndUsername(productId, "user");

        assertTrue("The saved item should be found", foundItem.isPresent());
        assertEquals(foundItem.get().getId(), savedItem.getId());
        assertEquals(foundItem.get().getProduct().getId(), productId);
        assertEquals(foundItem.get().getUsername(), "user");
    }

    @Test
    public void shouldNotFindShoppingCartItemByInvalidUsername() {
        final ShoppingCartItem item = createNewShoppingCartItem();

        final Long productId = (Long) entityManager.persistAndGetId(
                item.getProduct()
        );
        shoppingCartItemRepository.save(item);

        final Optional<ShoppingCartItem> foundItem =
                shoppingCartItemRepository.findByProductIdAndUsername(productId, "invaliduser");

        assertFalse(
                "The previously saved item should not be found",
                foundItem.isPresent()
        );
    }

    @Test
    public void shouldGetAllShoppingCartItemsOfAUser() {
        final int COUNT_OF_EXISTING_ITEMS = 7;
        Stream.generate(this::createNewShoppingCartItem)
                .limit(COUNT_OF_EXISTING_ITEMS)
                .forEach(item -> {
                    entityManager.persist(item.getProduct());
                    entityManager.persist(item);
                });

        final List<ShoppingCartItem> allItems =
                shoppingCartItemRepository.findByUsernameOrderByLastModifiedDateDesc("user");

        assertNotNull("The result should not be null", allItems);
        assertFalse("The result should not be empty", allItems.isEmpty());
        assertEquals(COUNT_OF_EXISTING_ITEMS, allItems.size());
    }

    @Test
    public void shouldGetAllOrderedByLastModifiedDate() {
        final List<ShoppingCartItem> existingItems =
                Stream.generate(this::createNewShoppingCartItem)
                        .limit(10)
                        .map(item -> {
                            entityManager.persist(item.getProduct());
                            return entityManager.persist(item);
                        })
                        .collect(Collectors.toList());

        final ShoppingCartItem firstItemToChange = existingItems.get(3);
        firstItemToChange.setQuantity(7);
        shoppingCartItemRepository.save(firstItemToChange);

        final ShoppingCartItem lastItemToChange = existingItems.get(9);
        lastItemToChange.setQuantity(7);
        shoppingCartItemRepository.save(lastItemToChange);

        final List<ShoppingCartItem> updateItems =
                shoppingCartItemRepository.findByUsernameOrderByLastModifiedDateDesc("user");

        assertEquals(
                "The last updated item should be the first in the list",
                lastItemToChange,
                updateItems.get(0)
        );
        assertEquals(
                "The first updated item should be the second in the list",
                firstItemToChange,
                updateItems.get(1)
        );
    }

    private ShoppingCartItem createNewShoppingCartItem() {
        final Product product = dataGenerator.generateProduct(false);
        return dataGenerator.generateShoppingCartItem(product, "user");
    }

    @After
    public void clearData() {
        entityManager.clear();
    }
}