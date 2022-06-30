package srowntree.clients;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface ServiceClient {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/")
    Response makeEditorRequest(@QueryParam("text") String pText);
}