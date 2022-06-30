package srowntree.tasks;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import srowntree.SlackHelper;
import srowntree.clients.ServiceClient;
import srowntree.clients.ServiceRegistryClient;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static srowntree.TestData.*;

@ApplicationScoped
public class ServiceMonitorTask implements Runnable {

    private static final String PROTOCOL = "http://";
    private static final String DOMAIN = ".40226640.qpc.hal.davecutting.uk";
    private static final String SERVICE_REGISTRY = "serviceregistry";


    @Override
    public void run() {
        doMonitoring();
    }

    private ServiceRegistryClient createRegistryClient() {
        String SERVICE_REGISTRY_URL = PROTOCOL + SERVICE_REGISTRY + DOMAIN;
        return RestClientBuilder.newBuilder()
                .baseUri(URI.create(SERVICE_REGISTRY_URL))
                .property("microprofile.rest.client.disable.default.mapper", true)
                .build(ServiceRegistryClient.class);
    }

    private ServiceClient createServiceClient(String pServiceName) {
        return RestClientBuilder.newBuilder()
                .baseUri(URI.create(PROTOCOL + pServiceName + DOMAIN))
                .property("microprofile.rest.client.disable.default.mapper", true)
                .build(ServiceClient.class);
    }

    public String doMonitoring() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String outputPrefix = formatter.format(date) + "\nIndividual Service Monitoring & Testing:\n";


        String output = "";


        for (String serviceType : SERVICE_TYPES) {

            ServiceRegistryClient registryClient = createRegistryClient();

            long startTime = System.currentTimeMillis();
            Response registryResponse = registryClient.getServices(serviceType);
            long responseTime = System.currentTimeMillis() - startTime;

            System.out.printf("Registry took %sms to respond for %s, %sms is timeout%n",
                    responseTime, serviceType, TOO_LONG_MILLIS);
            System.out.printf("Registry responded with status: %s for %s%n",
                    registryResponse.getStatus(), serviceType);

            if (responseTime > TOO_LONG_MILLIS) {
                output += "\n response time: " + responseTime + " was longer than " + TOO_LONG_MILLIS
                        + " from the registry when requesting services for type: " + serviceType
                        + "\n";
            }
            if (registryResponse.getStatus() != 200) {
                output += "\n status code: " + registryResponse.getStatus()
                        + " from the registry when requesting services for type: " + serviceType
                        + "\n";
                continue;
            }

            ArrayList<String> services = registryResponse.readEntity(ArrayList.class);

            for (String service : services) {
                ServiceClient serviceClient = createServiceClient(service);
                startTime = System.currentTimeMillis();
                Response serviceResponse = serviceClient.makeEditorRequest(
                        TEST_TEXT);
                responseTime = System.currentTimeMillis() - startTime;
                if (responseTime > TOO_LONG_MILLIS) {
                    output += "\n response time: " + responseTime + " was longer than " + TOO_LONG_MILLIS
                            + " for service request type: " + serviceType
                            + " to service " + service + "\n";
                } else System.out.println("service response received from " + service);

                if (serviceResponse.getStatus() != 200) {
                    output += "\n status code: " + serviceResponse.getStatus()
                            + " for service request type: " + serviceType
                            + " to service " + service + "\n";
                    continue;
                } else System.out.println("200 response received from " + service);
                JsonObject responseBody = serviceResponse.readEntity(JsonObject.class);
                int answer = responseBody.getInt("answer");
                if (serviceType.equals(WORD_COUNT)) {
                    if (answer != WORD_COUNT_ANSWER) {
                        output += "\n answer incorrect, expected " + WORD_COUNT_ANSWER + " but got " + answer
                                + " for service request type: " + serviceType
                                + " to service " + service + "\n";
                    } else System.out.println("correct answer from " + service);
                } else if (serviceType.equals(CHAR_COUNT)) {
                    if (answer != CHAR_COUNT_ANSWER) {
                        output += "\n answer incorrect, expected " + CHAR_COUNT_ANSWER + " but got " + answer
                                + " for service request type: " + serviceType
                                + " to service " + service + "\n";
                    } else System.out.println("correct answer from " + service);
                } else if (serviceType.equals(VOWEL_COUNT)) {
                    if (answer != VOWEL_COUNT_ANSWER) {
                        output += "\n answer incorrect, expected " + VOWEL_COUNT_ANSWER + " but got " + answer
                                + " for service request type: " + serviceType
                                + " to service " + service + "\n";
                    } else System.out.println("correct answer from " + service);
                } else if (serviceType.equals(PALINDROME_COUNT)) {
                    if (answer != PALINDROME_COUNT_ANSWER) {
                        output += "\n answer incorrect, expected " + PALINDROME_COUNT_ANSWER + " but got " + answer
                                + " for service request type: " + serviceType
                                + " to service " + service + "\n";
                    } else System.out.println("correct answer from " + service);
                } else if (serviceType.equals(SPELLING_ERRORS)) {
                    if (answer != SPELLING_ERROR_COUNT) {
                        output += "\n answer incorrect, expected " + SPELLING_ERROR_COUNT + " but got " + answer
                                + " for service request type: " + serviceType
                                + " to service " + service + "\n";
                    } else System.out.println("correct answer from " + service);
                } else if (serviceType.equals(COMMA_COUNT)) {
                    if (answer != COMMA_COUNT_ANSWER) {
                        output += "\n answer incorrect, expected " + COMMA_COUNT_ANSWER + " but got " + answer
                                + " for service request type: " + serviceType
                                + " to service " + service + "\n";
                    } else System.out.println("correct answer from " + service);
                }
            }
        }
        if (output.isBlank()) {
            System.out.println(outputPrefix+"NTR");
            SlackHelper.publishMessage((outputPrefix+"Nothing To Report"));
            return outputPrefix+"Nothing to report.";
        } else {
            System.out.println(outputPrefix+output);
            SlackHelper.publishMessage(outputPrefix+output);
            return outputPrefix+output;
        }
    }
}
