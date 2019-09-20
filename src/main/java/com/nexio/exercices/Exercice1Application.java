package com.nexio.exercices;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.AbstractApplicationContext;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@SpringBootApplication
public class Exercice1Application implements ApplicationRunner {

    private static final Logger LOG
            = Logger.getLogger(Exercice1Application.class.getName());

    @Autowired
    private AbstractApplicationContext appContext;

    @Autowired
    private ProductRepository productRepository;

    public static void main(String[] args) {
        LOG.info("L'appli pour l'exercice 1 a démarré!");
        SpringApplication.run(Exercice1Application.class, args);
        LOG.info("L'appli pour l'exercice 1 s'est actif!");
    }

    @PostConstruct
    public void initApplication() throws RuntimeException {
        // Grace à  on peut fermer l'application avec AbstractApplicationContext#doClose
        appContext.registerShutdownHook();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("number-of-products")) {
            Integer numberOfProducts = Integer.parseInt(
                    args.getOptionValues("number-of-products").get(0)
            );

            generateSampleProducts(numberOfProducts);
        }
    }

    private void generateSampleProducts(Integer amountOfProductsToGenerate) {
        for (int i = 0; i < amountOfProductsToGenerate; i++) {
            final Product product = DataGenerator.generateProductWithDetails(i % 2 == 0);
            final Product savedProduct = productRepository.save(product);
            LOG.info(String.format("Le produit \"%s\" a été ajouté avec succès", savedProduct));
        }
    }
}

