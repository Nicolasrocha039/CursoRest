package br.rs.nrfigueiredo.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.internal.path.xml.NodeImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.NamespaceContext;
import java.util.ArrayList;
import java.util.Locale;


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

    @Test
    public void devoFazerPesquisasAvancadasComXMLeJava() {
        ArrayList<NodeImpl> path = given()
                                 .when()
                                        .get()
                                 .then()
                                        .statusCode(200)
                                        .extract().path("users.user.name.findAll{it.toString().contains('n')}");

        Assert.assertEquals(2, path.size());
        Assert.assertEquals("Maria Joaquina".toUpperCase(), path.get(0).toString().toUpperCase());
        Assert.assertTrue("ANA JULIA".equalsIgnoreCase(path.get(1).toString()));
    }

    @Test
    public void devoFazerPesquisasAvancadasComXPath() {
        given()
        .when()
                .get()
        .then()
                .statusCode(200)
                .body(hasXPath("count(/users/user)", is("3")))
                .body(hasXPath("/users/user[@id = '1']"))
                .body(hasXPath("//user[@id = '2']"))
                .body(hasXPath("//name[text() = 'Zezinho']/../../name", is("Ana Julia")))
                .body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
                .body(hasXPath("/users/user/name", is("João da Silva")))
                .body(hasXPath("//name", is("João da Silva")))
                .body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
                .body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
                .body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))
                .body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
                .body(hasXPath("//user[age < 30 and age > 20]/name", is("Maria Joaquina")))
                .body(hasXPath("//user[age < 30][age > 20]/name", is("Maria Joaquina")))
        ;
    }
}
