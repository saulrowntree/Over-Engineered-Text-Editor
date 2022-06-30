package srowntree;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Objects;

@Path("/")
@RequestScoped
public class DatabaseResource {
    DatabaseProvider databaseProvider = new DatabaseProvider();

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSaveText(
            @DefaultValue("unknownParamDetected text") @QueryParam("text") String pText){

        if (pText.contains("unknownParamDetected"))
            return Responses.incorrectQueryParams(pText);

        long uId = System.nanoTime();
        databaseProvider.addTextToDb(uId, pText);

        return Responses.textSaved(uId);
    }

    @Path("/load")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoadText(
            @DefaultValue("unknownParamDetected ID") @QueryParam("UId") String pUId) {

        if (pUId.contains("unknownParamDetected"))
            return Responses.incorrectQueryParams(pUId);

        long longUID = 0;
        try {
            longUID = Long.parseLong(pUId);
        } catch (NumberFormatException cannotParse) {
            System.out.println("Couldn't Parse ID.");
        }
        String response = databaseProvider.selectByID(longUID);

        return response == null ? Responses.textNotFound(pUId) : Responses.textLoaded(response, longUID);
    }

    @Path("/saved")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getSavedText() {
        return databaseProvider.selectAll();
    }

    @Path("/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String clearSaved() {
        databaseProvider.deleteAll();
        return "cleared";
    }
}
