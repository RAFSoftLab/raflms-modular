package raflms.studentstub.restclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import raflms.studentstub.adapters.LocalDateAdapter;
import raflms.studentstub.dtos.AssignmentResponse;
import raflms.studentstub.dtos.TestDTO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public class TestRestClient {

    private final HttpClient httpClient;
    private final Gson gson;
    private final String testUrl;

    public TestRestClient(String baseURL) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
                .create();
        this.testUrl = baseURL + "/test";
    }

    public List<TestDTO> getAllTest() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(testUrl + "/all"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Request failed: " + response.statusCode());
            }

            Type listType = new TypeToken<List<TestDTO>>() {}.getType();
            return gson.fromJson(response.body(), listType);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AssignmentResponse> getAssignmentsForTestName(String testName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(testUrl + "/" + testName + "/assignments"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Request failed: " + response.statusCode());
            }

            Type listType = new TypeToken<List<AssignmentResponse>>() {}.getType();
            return gson.fromJson(response.body(), listType);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}