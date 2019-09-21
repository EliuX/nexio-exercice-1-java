EXERCICE 1 JAVA
====================

## Énoncé

Produisez un api REST qui permet les scénarios suivants:
* Afficher un catalogue de produits
* Afficher le détail d’un produit
* Ajouter un produit au panier
* Enlever un produit du panier
* Afficher le contenu du panier
* (Bonus) Connexion et déconnexion à un compte utilisateur
 
### Contraintes

* Utilisez une base de données SQL (in memory)
* Utilisez SpringBoot
* « Readme » clair permettant une installation simple et rapide du projet.
 
### Évaluation

Vous serez évalué sur les points suivants:
* Connaissance générale du contexte web et Java
* Architecture et utilisation de patrons de conception
* Qualité/Structure/Clarté générale du code
* Respect de l’énoncé
* test unitaire/intégration
 

## Solution

### Installation

Cet project utilise [Maven][maven-website] comme outil de gestion et 
d'automatisation. Donc, pour installer et démarcher le projet suivez le 
prochaines étapes:

1. Installez les dépendances    

```bash
mvn clean install
```

2. Exécutez l'application

Pour les essais automatiques le profile actif sera «test» et pendant 
l’exécution du dossier `jar` le profile actif sera «dev». Avec le 
profile «dev» l’application fera la génération automatique des _15_ 
produits pour remplir la base de données dans la mémoire. Cependant, si 
vous voulez changer cette quantité spécifiez le paramètre 
`number-of-products` 

...dans _resources/application.properties_:
```
number-of-products=20
```
ou pendant le démarche de l'appli:

```bash
java -jar target/nexio-exercice1-dev.jar --number-of-products=20
```

En outre, si vous voulez vous pouvez changer le packaging à `war` pour 
[livrer l'applis dans un serveur comme le Tomcat][demarche-tomcat]. Le
plugin `maven-war-plugin` est déjà configuré pour cet objectif.


### API REST
Les scénarios disponibles sont les suivants:

- Afficher un catalogue de produits: GET /products
- Afficher le détail d’un produit: GET /products/{productId}/details
- Ajouter un produit au panier: PUT /shopping-cart/items
- Afficher le contenu du panier: GET /shopping-cart/items
- Enlever un produit du panier: DELETE /shopping-cart/items

Il est recommandé utiliser la collection de Postman
`Nexio - Exercice1.postman_collection.json`.

### Author
**Eliecer Hernandez** - [eliecerhdz@gmail.com](mailto:eliecerhdz@gmail.com). 

Visitez ma [page LinkedIn][linkedin-page] pour en savoir plus sur moi.

[maven-website]: https://maven.apache.org/
[linkedin-page]: https://www.linkedin.com/in/eliecer-hern%C3%A1ndez-garbey-16172686/
[demarche-tomcat]: https://tomcat.apache.org/tomcat-8.0-doc/deployer-howto.html#Deployment_on_Tomcat_startup