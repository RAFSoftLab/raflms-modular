package raflms.studentstub.restclient;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ProjectFileClient {

    private final HttpClient httpClient;
    private final String projectUrl;

    public ProjectFileClient(String baseURL) {
        this.httpClient = HttpClient.newHttpClient();
        this.projectUrl = baseURL + "/project";
    }

    public String downloadFile(String assignmentRepoPath, String projectRoot) {

        try {
            String url = projectUrl + "/download?filePath=" + assignmentRepoPath;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<InputStream> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Download failed: " + response.statusCode());
            }

            String zadatakPath = projectRoot + "/zadatak.zip";

            try (InputStream in = response.body()) {
                Files.copy(in, Path.of(zadatakPath), StandardCopyOption.REPLACE_EXISTING);
            }

            return zadatakPath;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean uploadFile(String localFilePath, String remoteRepoPath, Boolean isFinalSubmission) {

        try {
            String boundary = UUID.randomUUID().toString();
            File file = new File(localFilePath);

            String url = projectUrl +
                    "/upload/studentproject?repoPath=" +
                    remoteRepoPath +
                    "&isFinalSubmission=" +
                    isFinalSubmission;

            byte[] fileBytes = Files.readAllBytes(file.toPath());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(bos, "UTF-8"), true);

            // multipart header
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                    .append(file.getName())
                    .append("\"\r\n");
            writer.append("Content-Type: application/octet-stream\r\n\r\n");
            writer.flush();

            bos.write(fileBytes);
            bos.flush();

            writer.append("\r\n--").append(boundary).append("--\r\n");
            writer.close();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(bos.toByteArray()))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Upload failed: " + response.statusCode());
            }

            return Boolean.parseBoolean(response.body());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}