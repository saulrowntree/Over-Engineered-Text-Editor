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

import java.util.ArrayList;

@HelidonTest
class MainTest {

    @Inject
    private WebTarget target;

    @Test
    void testAddAndGetServiceByName() {
        String testString = "testaddandgetservicebyname";
        Response response = target
                .path("/add")
                .queryParam("service", testString)
                .request()
                .post(null);
        String output = response.readEntity(String.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("0", output);

        response = target
                .path("/getservice")
                .queryParam("service", testString+0)
                .request()
                .get();
        output = response.readEntity(String.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals(testString+0, output);
    }
    @Test
    void testAddAndGetServicesByType() {
        String testString = "testaddandgetservicesbytype";
        Response response = target
                .path("/add")
                .queryParam("service", testString)
                .request()
                .post(null);
        String output = response.readEntity(String.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("0", output);

        response = target
                .path("/gettype")
                .queryParam("type", testString)
                .request()
                .get();
        String services = response.readEntity(String.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertTrue(services.contains(testString));
    }


    @Test
    void testAddAndDeleteService() {
        String testString = "testaddanddeleteservice";
        Response response = target
                .path("/add")
                .queryParam("service", testString)
                .request()
                .post(null);
        String output = response.readEntity(String.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertEquals("0", output);

        response = target
                .path("/delete")
                .queryParam("service", testString+0)
                .request()
                .delete();
        Assertions.assertEquals(200, response.getStatus());
    }
}
