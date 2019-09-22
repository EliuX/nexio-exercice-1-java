package com.nexio.exercices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.AbstractApplicationContext;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@SpringBootApplication
public class Exercice1Application {

    private static final Logger LOG
            = Logger.getLogger(Exercice1Application.class.getName());

    @Autowired
    private AbstractApplicationContext appContext;


    public static void main(String[] args) {
        LOG.info("L'appli pour l'exercice 1 a démarré!");
        SpringApplication.run(Exercice1Application.class, args);
        LOG.info("L'appli pour l'exercice 1 est actif!");
    }

    @PostConstruct
    public void initApplication() throws RuntimeException {
        // Grace à  on peut fermer l'application avec AbstractApplicationContext#doClose
        appContext.registerShutdownHook();
    }
}

