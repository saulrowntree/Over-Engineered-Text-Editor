package srowntree;

import javax.json.Json;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;

public class Responses {
    public static Response textSaved(long pUId) {
        return Response
                .status(200)
                .entity(Json.createBuilderFactory(Collections.emptyMap())
                        .createObjectBuilder()
                        .add("error", false)
                        .add("string", "Text saved, uniqueID is " + pUId + ".")
                        .add("answer", pUId)
                        .build())
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .build();
    }

    public static Response textLoaded(String pText, long pUId) {
        return Response
                .status(200)
                .entity(Json.createBuilderFactory(Collections.emptyMap())
                        .createObjectBuilder()
                        .add("error", false)
                        .add("string", pText)
                        .add("answer", pUId)
                        .build())
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .build();
    }

    public static Response incorrectQueryParams(String... pParams) {
        ArrayList<String> incorrectParams = new ArrayList<>();
        for (String param : pParams) {
            if (param.contains("unknown"))
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

    public static Response textNotFound(String pUId) {
        return Response
                .status(200)
                .entity(Json.createBuilderFactory(Collections.emptyMap())
                        .createObjectBuilder()
                        .add("error", false)
                        .add("string", "Uh oh! No text could be found for your ID.")
                        .add("answer", pUId)
                        .build())
                .header("Access-Control-Allow-Origin", "*")
                .header("Content-Type", "application/json")
                .build();
    }
}
