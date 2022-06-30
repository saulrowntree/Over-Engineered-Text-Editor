package srowntree;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import srowntree.clients.DatabaseClient;
import srowntree.clients.ServiceClient;
import srowntree.clients.ServiceRegistryClient;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;

@Path("/")
@RequestScoped
public class ProxyResource {

    private static final String DEFAULT_PROTOCOL = "http://";
    private static final String DEFAULT_DOMAIN = ".40226640.qpc.hal.davecutting.uk";
    private static final String DEFAULT_SERVICE_REGISTRY_URL = DEFAULT_PROTOCOL + "serviceregistry" + DEFAULT_DOMAIN;
    private static final String DEFAULT_DATABASE_URL = DEFAULT_PROTOCOL + "database" + DEFAULT_DOMAIN;

    @Retry(maxRetries = 3, maxDuration = 10000L)
    static Response forwardEditorRequest(URI pURL, String pQueryParam) {
        ServiceClient serviceClient = createServiceClient(pURL);
        return serviceClient.makeEditorRequest(pQueryParam);
    }

    private static ServiceClient createServiceClient(URI pURL) {
        return RestClientBuilder.newBuilder()
                .baseUri(pURL)
                .property("microprofile.rest.client.disable.default.mapper", true)
                .build(ServiceClient.class);
    }

    private static ServiceRegistryClient createServiceRegistryClient(URI pURL) {
        return RestClientBuilder.newBuilder()
                .baseUri(pURL)
                .property("microprofile.rest.client.disable.default.mapper", true)
                .build(ServiceRegistryClient.class);
    }

    private static DatabaseClient createDatabaseClient(URI pURL) {
        return RestClientBuilder.newBuilder()
                .baseUri(pURL)
                .property("microprofile.rest.client.disable.default.mapper", true)
                .build(DatabaseClient.class);
    }

    @Path("/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditorRequest(
            @DefaultValue("unknownParamDetected type") @QueryParam("type") String pType,
            @DefaultValue("unknownParamDetected text") @QueryParam("text") String pText) {

        if (pText.contains("unknownParamDetected") || pType.contains("unknownParamDetected")) {
            return Responses.incorrectQueryParams(pText, pType);
        }

        ServiceRegistryClient registryClient =
                createServiceRegistryClient(URI.create(DEFAULT_SERVICE_REGISTRY_URL));

        Response registryResponse = registryClient.getServices(pType);
        if (registryResponse.getStatus() != 200)
            return registryResponse;


        ArrayList urls = registryResponse.readEntity(ArrayList.class);
        ArrayList<String> services = (ArrayList<String>) urls;

        Response response = Responses.urlNotHeld(pType);
        for (String service : services) {
            URI absoluteURL = URI.create(DEFAULT_PROTOCOL + service + DEFAULT_DOMAIN);
            response = forwardEditorRequest(absoluteURL, pText);
            if (response.getStatus() == 200) {
                return response;
            }
        }
        return response;

    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAddService(
            @DefaultValue("unknownParamDetected service") @QueryParam("service") String pService) {

        ServiceRegistryClient registryClient =
                createServiceRegistryClient(URI.create(DEFAULT_SERVICE_REGISTRY_URL));

        return registryClient.addService(pService);
    }

    @Path("/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteURL(
            @DefaultValue("unknownParamDetected service") @QueryParam("service") String pService) {

        ServiceRegistryClient registryClient =
                createServiceRegistryClient(URI.create(DEFAULT_SERVICE_REGISTRY_URL));

        return registryClient.deleteService(pService);
    }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSaveText(
            @DefaultValue("unknownParamDetected text") @QueryParam("text") String pText) {

        DatabaseClient databaseClient = createDatabaseClient(URI.create(DEFAULT_DATABASE_URL));
        return pText.contains("unknownParamDetected") ? Responses.incorrectQueryParams(pText) :  databaseClient.saveText(pText);
    }

    @Path("/load")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoadText(
            @DefaultValue("unknownParamDetected ID") @QueryParam("UId") String pUId) {

        DatabaseClient databaseClient = createDatabaseClient(URI.create(DEFAULT_DATABASE_URL));
        return pUId.contains("unknownParamDetected") ? Responses.incorrectQueryParams(pUId) : databaseClient.loadSavedDataById(pUId);
    }

    @Path("/saved")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getSavedText() {
        DatabaseClient databaseClient = createDatabaseClient(URI.create(DEFAULT_DATABASE_URL));
        return databaseClient.loadAllSavedData();
    }

    @Path("/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String clearSaved(){
        DatabaseClient databaseClient = createDatabaseClient(URI.create(DEFAULT_DATABASE_URL));
        return databaseClient.clearAllSavedData();
    }
}
