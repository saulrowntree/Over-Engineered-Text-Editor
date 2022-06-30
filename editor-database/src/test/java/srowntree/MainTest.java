package srowntree;

import javax.inject.Inject;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
class MainTest {

    @Inject
    private WebTarget target;

    private JsonNumber uid;
    private String testString = "test string";

    @Test
    void testAddData() {
        Response response = target
                .path("/save")
                .queryParam("text", testString)
                .request()
                .post(null);
        Assertions.assertEquals(200, response.getStatus());
        JsonObject jsonObject = response.readEntity(JsonObject.class);
        uid = jsonObject.getJsonNumber("answer");
    }

    @Test
    void testLoadDataById() {
        Response response = target
                .path("/load")
                .queryParam("UId", uid)
                .request()
                .get();
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void testGetSavedText() {
        Response response = target
                .path("/saved")
                .request()
                .get();

        String responseString = response.readEntity(String.class);
        Assertions.assertEquals(200, response.getStatus());
        Assertions.assertTrue(responseString.contains(testString));
    }

    @Test
    void testClearSavedText() {
        Response response = target
                .path("/clear")
                .request()
                .post(null);
        Assertions.assertEquals(200, response.getStatus());
    }

}
