package srowntree;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Path("/")
@RequestScoped
public class RegistryResource {


    private static final String CONFIG_PATH = "URLs.properties";

    //private static final String CONFIG_PATH = "src/main/resources/URLs.properties";
    Properties mUrls = new Properties();

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAddService(
            @DefaultValue("unknownParamDetected service") @QueryParam("service") String pService) {

        if (pService.contains("unknownParamDetected")) {
            return Responses.incorrectQueryParams(pService);
        }
        int serviceNumber = addService(pService.toLowerCase(Locale.ROOT));
        return Responses.serviceAdded(serviceNumber);
    }


    public int addService(String pService) {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(CONFIG_PATH));
            int i = 0;
            while (prop.contains((pService + i))) i++;
            prop.setProperty(pService + i, pService + i);
            prop.store(new FileOutputStream(CONFIG_PATH), null);
            return i;
        } catch (
                IOException fileNotLoaded) {
            return -1;
        }
    }

    @Path("/delete")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteURL(
            @DefaultValue("unknownParamDetected type") @QueryParam("service") String pService) {

        if (pService.contains("unknownParamDetected"))
            return Responses.incorrectQueryParams(pService);

        return removeURLConfig(pService)
                ? Response.status(200).build()
                : Responses.configFileLoadError(CONFIG_PATH);
    }

    private boolean removeURLConfig(String pService) {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(CONFIG_PATH));
            prop.remove(pService);
            prop.store(new FileOutputStream(CONFIG_PATH), null);
        } catch (IOException fileNotLoaded) {
            return false;
        }
        return true;
    }

    @Path("/getservice")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getService(
            @DefaultValue("unknownParamDetected type") @QueryParam("service") String pService
    ) {
        if (loadURLs()) {
            String url = (String) mUrls.get(pService.toLowerCase(Locale.ROOT));
            if (url == null) {
                return Responses.urlNotHeld(pService);
            }
            return Responses.serviceFound(url);
        } else return Responses.configFileLoadError(CONFIG_PATH);
    }

    @Path("/gettype")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServicesByType(
            @DefaultValue("unknownParamDetected type") @QueryParam("type") String pType
    ) {
        ArrayList<String> services = new ArrayList<>();
        if (loadURLs()) {
            Map step1 = mUrls;
            Map<String, String> step2 = (Map<String, String>) step1;
            HashMap<String, String> urls = new HashMap<>(step2);
            for (String key : urls.keySet()) {
                if (key.contains(pType.toLowerCase()))
                    services.add(urls.get(key));
            }
            if (services.size() == 0) {
                return Responses.urlNotHeld(pType);
            }
            return Responses.servicesFound(services);
        } else return Responses.configFileLoadError(CONFIG_PATH);
    }

    @Path("/show")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServices() {
        loadURLs();
        return Response.status(200).entity(mUrls.toString()).build();
    }


    private boolean loadURLs() {
        boolean isLoaded;
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            mUrls.load(fis);
            isLoaded = true;
        } catch (IOException fallback) {
            try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
                mUrls.load(fis);
                isLoaded = true;
            } catch (IOException fileNotLoaded) {
                isLoaded = false;
            }
        }
        return isLoaded;
    }


}
