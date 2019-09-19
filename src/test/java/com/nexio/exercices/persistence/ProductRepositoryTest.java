package com.nexio.exercices.persistence;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.utils.Utils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    Product savedProduct;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    @Before
    public void persistProduct() {
        savedProduct = Utils.generateProductWithDetails(true);
        entityManager.persist(savedProduct);
    }

    @Test
    public void givenValidId_whenFindById_thenShouldReturnPerson() {
        final Optional<Product> productFound = productRepository.findById(savedProduct.getId());

        Assert.assertTrue(
                "The product should have been found",
                productFound.isPresent()
        );
    }

    @Test
    public void givenInvalidId_whenFindById_thenShouldNotReturnPerson() {
        final Optional<Product> productFound = productRepository.findById(savedProduct.getId() + 1);

        Assert.assertFalse(
                "The product should not have been found",
                productFound.isPresent()
        );
    }

    @Test
    public void shouldReturnMultipleProducts() {
        final Iterable<Product> allProducts = productRepository.findAll();

        Assert.assertTrue(
                "It was expected to have result",
                allProducts.iterator().hasNext()
        );
    }

    @After
    public void clearData() {
        entityManager.remove(savedProduct);
    }
}