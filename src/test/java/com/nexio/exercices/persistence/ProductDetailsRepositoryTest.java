package com.nexio.exercices.persistence;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.model.ProductDetails;
import com.nexio.exercices.utils.DataGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductDetailsRepositoryTest {
    Product savedProduct;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Before
    public void persistProduct() {
        savedProduct = DataGenerator.generateProductWithDetails(true);
        entityManager.persist(savedProduct);
    }


    @Test
    public void givenValidProductId_whenFindByProductId_thenReturnProductDetails() {
        final Optional<ProductDetails> foundProductDetails = productDetailsRepository.findByProductId(savedProduct.getId());

        Assert.assertTrue(
                "Expected to find a valid ProductDetails",
                foundProductDetails.isPresent()
        );

        Assert.assertNotNull(
                "Should contain reference to parent Product",
                foundProductDetails.get().getProduct()
        );
    }

    @Test
    public void givenInValidProductId_whenFindByProductId_thenDoNotReturnProductDetails() {
        final Optional<ProductDetails> foundProductDetails = productDetailsRepository.findByProductId(savedProduct.getId() + 1);

        Assert.assertFalse(
                "Expected not to find a valid ProductDetails",
                foundProductDetails.isPresent()
        );
    }


    @After
    public void clearData() {
        entityManager.remove(savedProduct);
    }
}