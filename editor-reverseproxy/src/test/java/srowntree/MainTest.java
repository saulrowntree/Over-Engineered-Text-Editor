package srowntree;

import javax.inject.Inject;
import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.spi.CDI;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import io.helidon.microprofile.server.Server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
class MainTest {

    @Inject
    private WebTarget target;

    @Test
    void testAddService() {
        Response response = target
                .path("/add")
                .queryParam("service", "blahservice")
                .request()
                .post(null);
        String output = response.readEntity(String.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("0", output);
    }

    @Test
    void testDeleteService() {
        Response response = target
                .path("/delete")
                .queryParam("service", "blahservice0")
                .request()
                .delete();
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void testWordCount() {
        Response response = target
                .path("/")
                .queryParam("type", "wordcount")
                .queryParam("text", "one two three")
                .request()
                .get(Response.class);
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertFalse(jsonObject.getBoolean("error"));
        Assertions.assertEquals(3, jsonObject.getInt("answer"));
    }

    @Test
    void testCharCount() {
        Response response = target
                .path("/")
                .queryParam("type", "charcount")
                .queryParam("text", "one two three")
                .request()
                .get(Response.class);
        System.out.println(response.getEntity().getClass());
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertFalse(jsonObject.getBoolean("error"));
        Assertions.assertEquals(13, jsonObject.getInt("answer"));
    }

    @Test
    void testPalindromeCount() {
        Response response = target
                .path("/")
                .queryParam("type", "palindromecount")
                .queryParam("text", "mom dad racecar")
                .request()
                .get(Response.class);
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertFalse(jsonObject.getBoolean("error"));
        Assertions.assertEquals(3, jsonObject.getInt("answer"));
    }

    @Test
    void testSpellingErrors() {
        Response response = target
                .path("/")
                .queryParam("type", "spellingerrors")
                .queryParam("text", "speljing mistajke")
                .request()
                .get(Response.class);
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertFalse(jsonObject.getBoolean("error"));
        Assertions.assertEquals(2, jsonObject.getInt("answer"));
    }
    @Test
    void testCommaCount() {
        Response response = target
                .path("/")
                .queryParam("type", "commacount")
                .queryParam("text", "one, two, three,")
                .request()
                .get(Response.class);
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertFalse(jsonObject.getBoolean("error"));
        Assertions.assertEquals(3, jsonObject.getInt("answer"));
    }
    @Test
    void testVowelCount() {
        Response response = target
                .path("/")
                .queryParam("type", "vowelcount")
                .queryParam("text", "aeiouaeiou")
                .request()
                .get(Response.class);
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertFalse(jsonObject.getBoolean("error"));
        Assertions.assertEquals(10, jsonObject.getInt("answer"));
    }
}
