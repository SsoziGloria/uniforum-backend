package GeneralUser.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private static final String BASE_URL = "http://127.0.0.1:8000/api";

    // --- GET Request (with optional token) ---
    public static String get(String endpoint, String token) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setInstanceFollowRedirects(false); // Prevent silent redirects to HTML pages

            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            
            // If Laravel redirects (e.g., 301/302), it means token authentication failed and it hit web routes
            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP || 
                responseCode == HttpURLConnection.HTTP_MOVED_PERM || 
                responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                String location = conn.getHeaderField("Location");
                return responseCode + ":{\"error\":\"Redirected to " + location + ". Check token/authentication.\"}" ;
            }

            BufferedReader br;
            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder responseStr = new StringBuilder();
            String responseLine;
            if (br != null) {
                while ((responseLine = br.readLine()) != null) {
                    responseStr.append(responseLine.trim());
                }
                br.close();
            }

            String finalResult = responseCode + ":" + responseStr.toString();
            System.out.println("API GET DEBUG [" + endpoint + "] -> " + finalResult);
            return finalResult;

        } catch (Exception e) {
            e.printStackTrace();
            return "500:Connection error. Is the Laravel server running? " + e.getMessage();
        }
    }

    // --- POST Request (Overload for Token Support) ---
    public static String post(String endpoint, String jsonPayload, String token) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            conn.setDoOutput(true);

            if (jsonPayload != null && !jsonPayload.isEmpty()) {
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            int responseCode = conn.getResponseCode();
            BufferedReader br;

            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder responseStr = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseStr.append(responseLine.trim());
            }

            return responseCode + ":" + responseStr.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "500:Connection error. Is the Laravel server running?";
        }
    }

    public static String post(String endpoint, String jsonPayload) {
        return post(endpoint, jsonPayload, null);
    }

    // --- DELETE Request ---
    // Added: previously there was no way to call any DELETE route at all
    // (e.g. leave group, delete group, delete message), so those actions
    // had no way to actually reach the server.
    public static String delete(String endpoint, String token) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Accept", "application/json");

            if (token != null && !token.isEmpty()) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }

            int responseCode = conn.getResponseCode();
            BufferedReader br;

            if (responseCode >= 200 && responseCode < 300) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder responseStr = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                responseStr.append(responseLine.trim());
            }
            br.close();

            return responseCode + ":" + responseStr.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "500:Connection error. Is the Laravel server running?";
        }
    }
}