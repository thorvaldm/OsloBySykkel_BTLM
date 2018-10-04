package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestClient {
    public String get(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Client-Identifier", "8bd26a8126b52b342989122b2b27dca2");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String response =  readResponse(br);
            conn.disconnect();
            return response;
        } catch (IOException e) {
            System.out.println("Klarer ikke å lese respons fra server!!");
            throw new RuntimeException("Failed: error message: " + e.getMessage());
        }
    }

    private String readResponse(BufferedReader br) throws IOException {
        String response = "";
        String output;
        while ((output = br.readLine()) != null) {
            response = response + output;
        }
        return response;
    }
}
