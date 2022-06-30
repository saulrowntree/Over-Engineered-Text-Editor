package srowntree;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;


@Path("/")
@RequestScoped
public class SpellingErrorsResource {

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response countSpellingErrors(@QueryParam("text") String text) {
        JsonObject result = (text == null
                ? SpellingCheckProvider.nullTextErrorResponse()
                : SpellingCheckProvider.countSpellingErrors(text));
        return Response.status(result.getBoolean("error") ? 400 : 200)
                .entity(result)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }
}