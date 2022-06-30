package srowntree.clients;
import org.eclipse.microprofile.faulttolerance.Retry;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface DatabaseClient {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/save")
    Response saveText(@QueryParam("text") String pText);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/load")
    Response loadSavedDataById(@QueryParam("UId") String pUId);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/saved")
    String loadAllSavedData();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/clear")
    String clearAllSavedData();


}