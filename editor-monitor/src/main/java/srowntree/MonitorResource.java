package srowntree;

import srowntree.tasks.ReverseProxyMonitorTask;
import srowntree.tasks.ServiceMonitorTask;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
@RequestScoped
public class MonitorResource {

    @Path("/proxy")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getProxyMonitoring() {
        ReverseProxyMonitorTask proxyMonitorTask = new ReverseProxyMonitorTask();
        return proxyMonitorTask.doMonitoring();
    }

    @Path("/service")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getServiceMonitoring() {
        ServiceMonitorTask serviceMonitorTask = new ServiceMonitorTask();
        return serviceMonitorTask.doMonitoring();
    }
}
