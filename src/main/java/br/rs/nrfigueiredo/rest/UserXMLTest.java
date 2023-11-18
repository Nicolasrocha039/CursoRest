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
                .get()
        .then()
                .statusCode(200)
                .body("users.user.size()", is(3))
                .body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
                .body("users.user.@id", hasItems("2", "1", "3"))
                .body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
                .body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Ana Julia", "Maria Joaquina"))
                .body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))
                .body("users.user.age.collect{it.toInteger() * 2}", hasItems(40,50,60))
                .body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))


        ;

    }
}
