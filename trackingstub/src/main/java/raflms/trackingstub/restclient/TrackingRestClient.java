package raflms.trackingstub.restclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import raflms.trackingstub.dtos.EventBatchDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrackingRestClient {

    private final HttpClient httpClient;
    private final Gson gson;
    private final String trackingUrl;

    public TrackingRestClient(String baseApiURL) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, ctx) ->
                                new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .create();
        this.trackingUrl = baseApiURL + "/events/batch";
    }

    public boolean sendEventBatch(EventBatchDto batch) {
        try {
            String requestBody = gson.toJson(batch);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(trackingUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("TrackingRestClient: server returned " + response.statusCode());
                return false;
            }

            return Boolean.parseBoolean(response.body());

        } catch (IOException | InterruptedException e) {
            System.err.println("TrackingRestClient: failed to send batch: " + e.getMessage());
            return false;
        }
    }
}