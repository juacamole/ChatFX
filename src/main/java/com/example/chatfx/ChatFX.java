package com.example.chatfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class ChatFX extends Application {

    private final String apiKey = "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";

    @Override
    public void start(Stage stage) {
        // Dropdown for model selection
        ComboBox<String> modelSelect = new ComboBox<>();
        modelSelect.getItems().addAll("gpt-3.5-turbo", "gpt-4");
        modelSelect.setValue("gpt-4");

        // Text field for user input
        TextField userQuery = new TextField();
        userQuery.setPromptText("Stelle deine Frage");

        // Button to send the request
        Button sendRequestButton = new Button("Frage OpenAI");

        // Container for response
        TextArea responseArea = new TextArea();
        responseArea.setEditable(false);

        // Layout
        VBox layout = new VBox(10, modelSelect, userQuery, sendRequestButton, responseArea);
        layout.setPadding(new Insets(15));

        // Button action
        sendRequestButton.setOnAction(event -> {
            String model = modelSelect.getValue();
            String query = userQuery.getText();
            sendRequestToOpenAI(model, query, responseArea);
        });

        // Scene
        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("OpenAI ChatGPT Anfrage");
        stage.setScene(scene);
        stage.show();
    }

    private void sendRequestToOpenAI(String model, String query, TextArea responseArea) {
        // HTTP Client
        HttpClient client = HttpClient.newHttpClient();

        // Prepare the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(
                        "{\"model\":\"" + model + "\",\"messages\":[{\"role\":\"system\",\"content\":\"You are a helpful assistant.\"},{\"role\":\"user\",\"content\":\"" + query + "\"}]}",
                        StandardCharsets.UTF_8))
                .build();

        // Send the request asynchronously and update the response area
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> responseArea.setText(response))
                .exceptionally(e -> { responseArea.setText("Error: " + e.getMessage()); return null; });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
