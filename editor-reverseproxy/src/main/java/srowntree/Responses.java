package srowntree;

import javax.json.Json;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;

public class Responses {
    public static Response urlNotHeld(String pType) {
        return Response
                .status(501)
                .entity(Json.createBuilderFactory(Collections.emptyMap())
                        .createObjectBuilder()
                        .add("error", true)
                        .add("string", "URL could not be found for the given function: " + pType + ".")
                        .add("answer", -1)
                        .build())
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .build();
    }

    public static Response incorrectQueryParams(String... pParams) {
        ArrayList<String> incorrectParams = new ArrayList<>();
        for (String param : pParams) {
            if (param.contains("unknownParamDetected"))
                incorrectParams.add(param.split(" ")[1]);
        }
        return Response
                .status(400)
                .entity(Json.createBuilderFactory(Collections.emptyMap())
                        .createObjectBuilder()
                        .add("error", true)
                        .add("string",
                                "The following query param(s) were not passed properly: " + incorrectParams)
                        .add("answer", -1)
                        .build())
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .build();
    }
}
