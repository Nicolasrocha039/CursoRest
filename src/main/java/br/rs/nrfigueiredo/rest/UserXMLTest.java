package br.rs.nrfigueiredo.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;


public class UserXMLTest extends BaseTest {

    @Before
    public void before1() {
        baseURI = "https://restapi.wcaquino.me";
        basePath = "/usersXML";
    }


    @Test
    public void devoTrabalharComXML() {
        given()
        .when()
                .get("/3")
        .then()
                .statusCode(200)
                .rootPath("user")
                .body("name", is("Ana Julia"))
                .body("@id", is("3"))
                .appendRootPath("filhos.name")
                .body("size()", is(2))
                .body("[0]", is("Zezinho"))
                .body("[1]", is("Luizinho"))
                .body("", hasItem("Luizinho"))
                .body("", hasItems("Luizinho", "Zezinho"))

        ;

    }

    @Test
    public void devoFazerPesquisasAvancadasComXML() {
        given()
        .when()
                .get("/3")
        .then()
                .statusCode(200)
                

        ;

    }
}
