package br.rs.nrfigueiredo.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VerbosTest {

    @Before
    public void before() {
        baseURI = "https://restapi.wcaquino.me";
        basePath = "/users";
    }

    @Test
    public void deveAdicionarUmUser() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{ \"name\": \"José Almeida\", \"age\": 35 }")
        .when()
                .post()
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("José Almeida"))
                .body("age", is(35))
        ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{ \"age\": 35 }")
        .when()
                .post()
        .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveAdicionarUmUserComXML() {
        basePath = "/usersXML";

        given()
                .log().all()
                .contentType(ContentType.XML)
                .body("<user><name>Jose Almeida</name><age>35</age></user>")
        .when()
                .post()
        .then()
                .log().all()
                .statusCode(201)

        ;
    }

    @Test
    public void deveAdicionarUmUserComXMLUsandoObjeto() {
        User user = new User("Usuario XML", 69);
        basePath = "/usersXML";

        given()
                .log().all()
                .contentType(ContentType.XML)
                .body("<user><name>Jose Almeida</name><age>35</age></user>")
        .when()
                .post()
        .then()
                .log().all()
                .statusCode(201)

        ;
    }

    @Test
    public void deveAlterarUmUser() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{ \"name\": \"Josi da Silva\", \"age\": 150 }")
        .when()
                .put("/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Josi da Silva"))
                .body("age", is(150))
                .body("salary", is(1234.5678f))
        ;
    }

    @Test
    public void deveRemoverUser() {
        given()
                .log().all()
        .when()
                .delete("/1")
        .then()
                .log().all()
                .statusCode(204)
        ;
    }

    @Test
    public void deveAdicionarUmUserComMAP() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Usuario via map");
        params.put("age", 125);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(params)
        .when()
                .post()
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Usuario via map"))
                .body("age", is(125))
        ;
    }

    @Test
    public void deveAdicionarUmUserComObjeto() {
        User usuario;
        usuario = new User("Sao pedro", 85);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(usuario)
        .when()
                .post()
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Sao pedro"))
                .body("age", is(85))
        ;
    }

    @Test
    public void deveDesserializarUmUser() {
        User usuario;
        usuario = new User("Sao jorge", 97);


        User usuarioInserido =  given()
                                        .log().all()
                                        .contentType(ContentType.JSON)
                                        .body(usuario)
                                .when()
                                        .post()
                                .then()
                                        .log().all()
                                        .statusCode(201)
                                        .extract().body().as(User.class)
        ;

        System.out.println(usuarioInserido);
        Assert.assertThat(usuarioInserido.getId(), notNullValue());
        Assert.assertEquals("Sao jorge", usuarioInserido.getName());
        Assert.assertThat(usuarioInserido.getAge(), is(97));

    }
}

