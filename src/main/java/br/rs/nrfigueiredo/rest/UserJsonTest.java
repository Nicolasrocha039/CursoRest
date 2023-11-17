package br.rs.nrfigueiredo.rest;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;


public class UserJsonTest {

    @Before
    public void before() {
        baseURI = "https://restapi.wcaquino.me";
        basePath = "/users";
    }

    @Test
    public void deveVerificarPrimeiroNivel() {

        when().
                get("/1").
        then().
                statusCode(200).
                body("id", is(1)).
                body("name", containsString("Silva")).
                body("age", greaterThan(18));
    }

    @Test
    public void deveVerificarPrimeiroNivelOutrasFormas() {
        Response response = request(Method.GET, "/1");

        // path
        Assert.assertEquals(new Integer(1), response.path("id"));
        Assert.assertEquals(new Integer(1), response.path("%s", "id"));

        // jsonpath
        JsonPath jp = new JsonPath(response.asString());
        Assert.assertEquals(1, jp.getInt("id"));

        // from
        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);

    }

    @Test
    public void deveVerificarSegundoNivel() {

        when().
                get("/2").
        then().
                statusCode(200).
                body("name", containsString("Joaquina")).
                body("endereco.rua", is("Rua dos bobos")).
                body("endereco.numero", is(0));

    }

    @Test
    public void deveVerificarLista() {

        when().
                get("/3").
        then().
                statusCode(200).
                body("name", containsString("Ana")).
                body("filhos", hasSize(2)).
                body("filhos[0].name", is("Zezinho")).
                body("filhos[1].name", is("Luizinho")).
                body("filhos.name", hasItem("Luizinho")).
                body("filhos.name", hasItems("Zezinho", "Luizinho"))
        ;
    }

    @Test
    public void deveRetornarUsuarioInexistente() {

        when().
                get("/4").
        then().
                statusCode(404).
                body("error", is("Usuário inexistente"))
        ;
    }

    @Test
    public void deveVerificarListaNaRaiz() {

        when().
                get().
        then().
                statusCode(200).
                body("", hasSize(3)).
                body("id", hasItems(1,2,3)).
                body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia")).
                body("age", hasItems(30, 25, 20)).
                body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho"))).
                body("salary", contains(1234.5678f, 2500, null))
        ;
    }

    @Test
    public void deveFazerVerificacoesAvancadas() {

        when().
                get().
        then().
                statusCode(200).
                body("$", hasSize(3)).
                body("age.findAll{it <= 25}.size()", is(2)).
                body("age.findAll{it <= 25 && it > 20}.size()", is(1)).
                body("age.findAll{it >= 20 && it <= 30}.size()", is(3)).
                body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina")).
                body("findAll{it.age <= 25}[0].name", is("Maria Joaquina")).
                body("findAll{it.age <= 25}[-1].name", is("Ana Júlia")).
                body("find{it.age <= 25}.name", is("Maria Joaquina")).
                body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia")).
                body("findAll{it.name.length() > 10}.name", hasItems("Maria Joaquina", "João da Silva")).
                body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA")).
                body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA")).
                body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1))).
                body("age.collect{it * 2}", hasItems(60, 50, 40)).
                body("id.max()", is(3)).
                body("salary.min()", is(1234.5678f)).
                body("salary.findAll{it != null}.sum()", is(closeTo(3734.5679f, 0.001))).
                body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d)))
        ;
    }

    @Test
    public void devoUnirJsonPathComJava() {
        ArrayList<String> nomes =
            given().
            when().
                    get().
            then().
                    statusCode(200).
                    extract().
                    path("name.findAll{it.startsWith('Maria')}")
        ;

        Assert.assertEquals(1, nomes.size());
        Assert.assertTrue(nomes.get(0).equalsIgnoreCase("mAria Joaquina"));
        Assert.assertEquals(nomes.get(0).toUpperCase(), "maria joaquina".toUpperCase());
    }


}
