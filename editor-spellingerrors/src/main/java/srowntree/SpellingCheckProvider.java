package srowntree;

import javax.json.JsonObject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class SpellingCheckProvider {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
    private static final Path mPathToDictionary = Path.of("words.txt");
    private static final String mBlankMessageString = "There's no spelling errors in a blank string...";

    public static JsonObject countSpellingErrors(String pText) {
        try {
            int wrongCount = 0;
            pText = pText.replaceAll("[^\\p{Alpha} ]", "");
            String[] words = pText.split(" ");

            for (String word : words) {
                if (word.isEmpty() || word.isBlank())
                    continue;
                wrongCount += Files
                        .lines(mPathToDictionary, StandardCharsets.US_ASCII)
                        .noneMatch(n -> n.equalsIgnoreCase(word))
                        ? 1 : 0;
            }
            return validResponse(pText, wrongCount);
        } catch (Exception e) {
            return errorResponse(pText, e);
        }
    }

    public static JsonObject nullTextErrorResponse() {
        return JSON.createObjectBuilder()
                .add("error", true)
                .add("string", "text param was not passed correctly.")
                .add("answer", -1)
                .build();
    }

    public static JsonObject validResponse(String pText, int answer) {
        return JSON.createObjectBuilder()
                .add("error", false)
                .add("string",
                        pText.isBlank() ? mBlankMessageString
                                : "Input text contains " + answer + " spelling mistakes")
                .add("answer", answer)
                .build();
    }

    public static JsonObject errorResponse(String pText, Exception e) {
        return JSON.createObjectBuilder()
                .add("error", true)
                .add("input-string", pText)
                .add("string", "An error was encountered and an answer cannot be given")
                .add("error-message", e.getMessage())
                .add("answer", -1)
                .build();
    }
}

