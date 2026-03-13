package raflms.studentstub.restclient;

import com.google.gson.Gson;
import raflms.studentstub.dtos.StudentAssignmentResponse;
import raflms.studentstub.dtos.StudentStartAssignmentRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AssigmentRestClient {

    private final HttpClient httpClient;
    private final Gson gson;

    private final String submissionUrl;

    public AssigmentRestClient(String baseURL) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.submissionUrl = baseURL + "/student/submission";
    }

    public StudentAssignmentResponse startAssignment(StudentStartAssignmentRequest request)
            throws IOException, InterruptedException {

        String requestBody = gson.toJson(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(submissionUrl + "/authorizeforasignment"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response =
                httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Request failed with status: " + response.statusCode());
        }

        return gson.fromJson(response.body(), StudentAssignmentResponse.class);
    }
}