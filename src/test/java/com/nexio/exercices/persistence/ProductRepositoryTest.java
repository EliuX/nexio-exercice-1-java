package com.nexio.exercices.persistence;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    @Before
    public void clearData() {
        entityManager.clear();
    }

    @Test
    public void shouldReturnExistingProducts() {
        final int COUNT_OF_EXISTING_PRODUCTS = 3;
        Stream.generate(() -> DataGenerator.generateProductWithDetails(false))
                .limit(COUNT_OF_EXISTING_PRODUCTS)
                .forEach(entityManager::persist);

        final List<Product> existingProducts = productRepository.findAll();

        assertNotNull("The result should not be null", existingProducts);
        assertFalse("The result should not be empty", existingProducts.isEmpty());
        assertEquals(
                "The result should have the expected size",
                COUNT_OF_EXISTING_PRODUCTS,
                existingProducts.size()
        );
    }

    @Test
    public void shouldNotReturnAnyProduct() {
        final List<Product> existingProducts = productRepository.findAll();

        assertTrue("The result should be empty", existingProducts.isEmpty());
    }
}