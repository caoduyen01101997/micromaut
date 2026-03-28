package com.example.service;

import jakarta.inject.Singleton;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Singleton
public class PumpService {

    private static final String FIREBASE_URL = "https://autowaterplant-3d78d-default-rtdb.asia-southeast1.firebasedatabase.app";
    private static final String PUMP_STATE_URL = FIREBASE_URL + "/pump/state.json";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void turnOn() {
        setPumpStatus("ON");
    }

    public void turnOff() {
        setPumpStatus("OFF");
    }

    public boolean getStatus() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PUMP_STATE_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body().trim().replace("\"", "");
            System.out.println("🔍 Firebase pump state: " + body);
            return "ON".equalsIgnoreCase(body);
        } catch (Exception e) {
            System.err.println("❌ Cannot read pump status: " + e.getMessage());
            return false;
        }
    }

    private void setPumpStatus(String state) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PUMP_STATE_URL))
                    .PUT(HttpRequest.BodyPublishers.ofString("\"" + state + "\""))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("✅ Pump status set to: " + state);
            } else {
                System.err.println("❌ Firebase write failed: HTTP " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("❌ Cannot update pump status: " + e.getMessage());
        }
    }
}
