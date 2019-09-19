<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="fr-CA">
<head>
    <meta charset="UTF-8">
    <meta Content-Language="fr">
    <title>Détails du produit : ${product.name}</title>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="/css/custom.css" rel="stylesheet">
</head>
<body class="d-flex flex-column h-100">
<main role="main" class="flex-shrink-0">
    <section class="container">
        <header>
            <h1 class="mt-5">${product.name}</h1>
            <p class="lead">
                Détails du Produit
            </p>
        </header>
        <section>
            <c:choose>
                <c:when test="${empty details}">
                    <div class="alert alert-info" role="alert">
                        <strong>Pas de détails!</strong> Il semble que ce produit n'a rien d'intéressant que montrer.
                    </div>
                </c:when>
                <c:otherwise>
                    <div id="details-data" class="container">
                        <div class="row">
                            <div class="col-sm-2 col-md-auto"><strong>Est-il comestible?</strong></div>
                            <div class="col-md-8">
                                    ${details.edible ? "Oui, utilisez-le comme ingrédiant" : "Non, c\'est pour nourrir ta tête"}
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12 col-md-auto"><strong>Description</strong></div>
                        </div>
                        <div class="row">
                            <div class="col col-lg-8">
                                    ${details.description}
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
    </section>
    <section class="container">
        <nav class="navbar navbar-light bg-faded">
            <a href="/products">Voir d'autres produits</a>
        </nav>
    </section>
</main>

<footer class="footer mt-auto py-3">
    <div class="container">
        <span class="text-muted">Nexio 2019</span>
    </div>
</footer>
</body>
</html>