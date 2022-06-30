package test;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.WebTarget;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.helidon.microprofile.tests.junit5.HelidonTest;

@HelidonTest
class SpellingErrorTest {

    @Inject
    private WebTarget target;

    @Test
    void testSpellingErrorResponse() {
        Response r = target
                .path("/")
                .request()
                .get(Response.class);
        Assertions.assertEquals(400, r.getStatus());

    }
    @Test
    void testSpellingErrorCountMistakeInString(){
        Response r = target
                .path("/")
                .queryParam("text","test string mistajke string test")
                .request()
                .get(Response.class);
        JsonObject jsonObject = r.readEntity(JsonObject.class);
        Assertions.assertEquals(1, jsonObject.getInt("answer"));
    }

    @Test
    void testSpellingErrorCountYearInString(){
        Response r = target
                .path("/")
                .queryParam("text","test string 1985 string test")
                .request()
                .get(Response.class);
        JsonObject jsonObject = r.readEntity(JsonObject.class);
        Assertions.assertEquals(0, jsonObject.getInt("answer"));
    }
    @Test
    void testSpellingErrorCountNoMistakesInString(){
        Response r = target
                .path("/")
                .queryParam("text","test string 1985 string test")
                .request()
                .get(Response.class);
        JsonObject jsonObject = r.readEntity(JsonObject.class);
        Assertions.assertEquals(0, jsonObject.getInt("answer"));
    }
}
