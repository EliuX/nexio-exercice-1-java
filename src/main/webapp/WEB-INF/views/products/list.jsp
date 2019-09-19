<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="fr-CA">
<head>
    <meta charset="UTF-8">
    <meta Content-Language="fr">
    <title>Catalogue de produits</title>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link href="/css/custom.css" rel="stylesheet">
</head>
<body class="d-flex flex-column h-100">
<main id="product-catalog" role="main" class="flex-shrink-0">
    <div class="container">
        <h1 class="mt-5">Catalogue des produits</h1>
        <p class="lead">
            Vous trouverez a continuation les produits qui sont disponibles:
            Ils seront des livres ou des aliments comestibles
        </p>

        <div class="table-responsive-md">
            <c:choose>
                <c:when test="${empty products}">
                    <div class="alert alert-info" role="alert">
                        <strong>Pas de produits!</strong> Il semble que la base de données n'a pas été alimenté.
                    </div>
                </c:when>
                <c:otherwise>
                    <c:set var="countOfProducts" value="${fn:length(products)}"/>
                    <table class="table">
                        <caption>Il y à ${fn:length(products)} produits disponibles</caption>
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Nom</th>
                            <th scope="col">Prix</th>
                            <th scope="col">Type</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach varStatus="cursor" var="product" begin="0" end="${countOfProducts}"
                                   items="${products}">
                            <tr>
                                <th scope="row">${cursor.index + 1}</th>
                                <td><a title="Voir des détails"
                                       href="/products/${product.id}/details">${product.name}</a>
                                </td>
                                <td>${product.price} CAD</td>
                                <td>${product.productDetails.edible ? "Comestible" : "Livre"}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<footer class="footer mt-auto py-3">
    <div class="container">
        <span class="text-muted">Nexio 2019</span>
    </div>
</footer>
</body>
</html>