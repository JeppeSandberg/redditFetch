package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        String subreddit = "leagueoflegends"; // Example subreddit
        String[] postIds = {"192mnsu", "19dw626", "1akhtig", "1b7dqlu"};
                            // Patch notes 11.1, 11.2, 11.3, 11.4

        for (String postId : postIds) {
            String endpointURL = "https://www.reddit.com/r/" + subreddit + "/comments/" + postId + ".json";
            try {
                // Create a BufferedWriter to write data to a text file
                BufferedWriter writer = new BufferedWriter(new FileWriter(postId + "_comments.txt"));

                // Convert String URL to URI, then to URL
                URI uri = new URI(endpointURL);
                URL url = uri.toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the JSON response from the API
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                // Parse the JSON response to extract comments
                JSONArray commentsArray = new JSONArray(response.toString()).getJSONObject(1).getJSONObject("data").getJSONArray("children");
                for (int j = 0; j < commentsArray.length(); j++) {
                    JSONObject comment = commentsArray.getJSONObject(j).getJSONObject("data");
                    String commentText = comment.optString("body", ""); // Some comments might not have a body
                    writer.write(commentText + "\n\n"); // Add newlines for readability
                }

                // Close the BufferedWriter
                writer.close();

                // Disconnect the connection
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}