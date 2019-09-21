package com.nexio.exercices.configuration;

import com.nexio.exercices.model.Product;
import com.nexio.exercices.persistence.ProductRepository;
import com.nexio.exercices.utils.DataGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Logger;

@Configuration
public class AppConfig implements ApplicationRunner {

    private static final Logger LOG
            = Logger.getLogger(AppConfig.class.getName());

    private static final Locale DEFAULT_LOCALE = Locale.CANADA_FRENCH;

    @Autowired
    private Environment env;

    @Autowired
    private ProductRepository productRepository;

    @Value("${data.number-of-products:15}")
    private Integer defaultNumberOfProducts;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(DEFAULT_LOCALE);
        return slr;
    }

    @Bean
    public DataGenerator dataGenerator() {
        return new DataGenerator(DEFAULT_LOCALE);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("data.number-of-products")) {
            Integer numberOfProducts = Integer.parseInt(
                    args.getOptionValues("data.number-of-products").get(0)
            );

            generateSampleProducts(numberOfProducts);
        } else if (isActiveProfile(Profiles.DEVELOPMENT)) {
            generateSampleProducts(defaultNumberOfProducts);
        }
    }

    private void generateSampleProducts(Integer amountOfProductsToGenerate) {
        for (int i = 0; i < amountOfProductsToGenerate; i++) {
            final Product product = dataGenerator().generateProductWithDetails(
                    i % 2 == 0
            );
            final Product savedProduct = productRepository.save(product);
            LOG.info(String.format(
                    "Le produit \"%s\" a été ajouté avec succès",
                    savedProduct)
            );
        }
    }

    private boolean isActiveProfile(String profileName) {
        return Arrays.toString(env.getActiveProfiles())
                .contains(profileName);
    }
}
