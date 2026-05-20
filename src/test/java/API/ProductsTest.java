package API;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import Utilities.LoadResource;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import static Utilities.CSVdataLoader.readAsMaps;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductsTest {
    @BeforeAll
    static void setUp() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.baseURI = "https://fakestoreapi.com/";
    }

    @Test
    void getAllProducts() {
        given()
                .log().all()
                .when().get("/products")
                .then()
                .log().all()
                .statusCode(200)
                .body("[0].title", notNullValue());
    }

    @Test
    void getProductById() {
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when().get("/products/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("price", notNullValue());

    }

    @Test
    void allPricesShouldBeGreaterThanZero() {

        List<Float> prices =
                given()
                        .when()
                        .get("/products")
                        .then()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList("price", Float.class);

        for (Float price : prices) {
            System.out.println("Precio: " + price);
            assertTrue(price > 0);
        }
    }

    @Test
    void postProductPayload() {

        String body = LoadResource.loadResource("PayloadJson/Products.json");
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(body)
                .when().post("/products")
                .then()
                .log().all()
                .statusCode(201)
                .body("title", equalTo("Camiseta Ibiza"));

    }

   @Test
   void putProductPayload() {
        String body = LoadResource.loadResource("PayloadJson/UpdateProducts.json");
       given()
               .log().all()
               .header("Content-Type", "application/json")
               .header("Accept", "application/json")
               .body(body)
               .when().put("/products/21")
               .then()
               .log().all()
               .statusCode(200)
               .body("price", equalTo(25));
   }

   @Test
   void deleteProductPayload() {
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .when().delete("/products/21")
                .then()
                .log().all()
                .statusCode(200);
   }

    @Test
    void postProductsPlantilla() throws IOException {
        var rows = readAsMaps("CSV/ProductsCSV.csv", ',');
        Map<String, String> row = rows.get(0);
        String body = LoadResource.loadResource("PayloadJson/ProductsPlantilla.json.tpl");
        body = body.replace("{{title}}", row.get("title"))
                .replace("{{price}}", row.get("price"))
                .replace("{{description}}", row.get("description"))
                .replace("{{category}}", row.get("category"))
                .replace("{{id}}", row.get("id"))
                .replace("{{image}}", row.get("image"));
        given()
                .log().all()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(body)
                .when().post("/products")
                .then()
                .log().all()
                .statusCode(201);
                //.body("title", equalTo("Camiseta Ibiza"));
    }
    @AfterAll
    static void tearDown() {
    }
}
