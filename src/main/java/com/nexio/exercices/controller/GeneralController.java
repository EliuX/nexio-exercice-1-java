package com.nexio.exercices.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {

    @RequestMapping("/")
    public String showWelcomeMessage() {
        return "Bienvenue à la solution de Nexio - Exercice 1";
    }
}