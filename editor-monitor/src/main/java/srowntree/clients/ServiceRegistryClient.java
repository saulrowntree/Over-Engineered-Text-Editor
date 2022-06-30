package srowntree.clients;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public interface ServiceRegistryClient {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/gettype")
    Response getServices(@QueryParam("type") String pType);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    Response addService(@QueryParam("service") String pService);

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete")
    Response deleteService(@QueryParam("service") String pService);
}