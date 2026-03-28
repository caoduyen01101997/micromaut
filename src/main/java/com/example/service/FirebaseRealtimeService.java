package com.example.service;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Singleton
public class FirebaseRealtimeService {

    private static final String DATABASE_URL = "https://autowaterplant-3d78d-default-rtdb.asia-southeast1.firebasedatabase.app";
    private FirebaseApp firebaseApp;
    @Value("${firebase.service-account-base64}")
    String serviceAccountBase64;

    @PostConstruct
    public void init() {
        try {
            String base64 = "ewogICJ0eXBlIjogInNlcnZpY2VfYWNjb3VudCIsCiAgInByb2plY3RfaWQiOiAiYXV0b3dhdGVycGxhbnQtM2Q3OGQiLAogICJwcml2YXRlX2tleV9pZCI6ICI4OTZkOTYzODI4YjFjMWU4MjA2MjBiZmUyMDJlOGNkMjc3N2VjYzY2IiwKICAicHJpdmF0ZV9rZXkiOiAiLS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tXG5NSUlFdkFJQkFEQU5CZ2txaGtpRzl3MEJBUUVGQUFTQ0JLWXdnZ1NpQWdFQUFvSUJBUURwUzNRaWU1WWo5NXJKXG4zNGE4YVVMN0FHR3BJY2psTm03ZXBFVHlYTlEzL0hKcm5qZTJzaERaMnEyUVhuaUF5NVdhRGhRaVZLVzNNaXVrXG5pT3NXZjBtcWRhUGZzSFI5T1BkRHNEUWhzL09xZzg0K1UxTFNscjBVanZVc21tU0h3dG1LSjFqekFhU0JGYzc4XG4zZ2pXZmZpVmZ5aGE4Z29ySk9FTi82YVF4UFFWeUhoSzJZWWZNaGs2bW44b1JReFdlcWw1TTF0MmhzTEpTV3AxXG5qU2JHaURLMFVMR1c0b1dlUVY5NkQvcDNRQm1ZbENNOTVibUM5L1ZGV1l3WW9ReTFoOW82MzlyL0V6dEIyT0hLXG5tT1YxSm40LzFLcGNsK0hQL3pNZDJyNU1FdzZqRHdRRHN3eG9CY2R5eWJBeGRkcDYvOWlVcHFJZWVvY3VFM0I1XG5oQlpzVE5NVkFnTUJBQUVDZ2dFQUcrZ1dOcy9BeUlHa1p2U1JtZlVmZVlYMHFMWXRZVm5QRGx2cEVYTFFRZmcxXG5PYkZkNmNGSUtiOHlWdHd1SHYzN1ljZkFXZEQxVjhJdEhVdnhqeW5meG4rM0FCbUwvYlMwUTBrUEcxNzZ6VkZoXG4rT3Vmc3lncjZ3c2t3ZGFnSXV0SWVYRzJmcThhSGdUZFV6cytKR1pDTjZQVDBIazJoUzJ1ckMyQ0g3d25sSGxCXG5qamVIN0tTMHMvRDE1N3VDWHdyUGlveEIwb0lmK0RQcnczUzc4Y0JkRkIzdUlQV1NSK1RyS1RYblJ1YlRDUmtHXG5UcEo3TkhSeG8rYWpxcTQxS1Q0V2oyUnNjbU1OcDdObkEwQ1dQdGNDb1dGQ1dINWpSWXZCWHc2SC9XelpXak5UXG52ZTV5T1cvaGkwTXhFMkl1ZzBEZHptWGplOG9CNmVnM3lVd0Zva0lMT3dLQmdRRDJ2cGNIcXcwcHh6eWdxZUlZXG56dzA2Sk45cTZsajdySmRHVTZ1eHptaG1UMXpnRWNTaDhlNUhCQnJPVS9RTG1pb096UlppUzZwcXFvRkVSSlorXG5DNDVkYllMWHVrM1BuK1RIdFN4OEhpa3lIK1hTdFd1TGM3cDhneGlRekE2bzVsU2VjRzltaGFUNDA5SE9TOHFTXG5ObEpURXlrcXArdmo2cFdmMFEzTnB3OFo0d0tCZ1FEeUM3Vys1SmFJQXJ2VzNNZHRncmdkVGViQzYyK0RhRU53XG4rVksxVE9NTWovVjkvZUtnOWJYa1VwY1BpZENKNDlURWJyRzYwUGtmOFNCK3hzVmg3WlF5bkVBaWNiUmpzR3I1XG4yUmNBaE9rWjQyMDEzd1d1YXN5UHQrZ0tNNGNiWFE4YktYeno0b201Z1RZMXhQa056U1hKMkNxMHdFS1g5NzVhXG5CUGJJVC9SUXB3S0JnUUNrMTJsQTF6YWhEWmprWVBwS3hyQzRnbis4QnJuUjBWanl3OTAxbVliSHF3ZUtMblNtXG5HR0d1a2dCeWpVWENKb2tvZ0QwUENDdmV2UEJGTW1rejVmYURWakVPZkVtVmh1V3B3ZERwVHFFSStxUm9ZNGxvXG5qYlV0ajh0dW1OVEdZN0p1Y3dFSmhnbFRydmRBa2dQUU1IaEpSY2kvMVpWVEc1cEFQY0t4L0lTckx3S0JnUUNhXG5KMEVTR2EyUlpBUlQzSHFhMFZVSFBOVUtNVDN0T0JzenVXYW1seGhIcU92VjJUdGIvZ0ZoTUFTTFhkOVY0cWRnXG5qNHRjQ3JtUFVWZTd4a1p4andrQ2pxYitDT0sxVnVkYXhwc3l0bk5yRElCQ2ZnNjFQY21ZZ3EwaXhleHpIZnAvXG45eFZna2xmYXNHTmhMVmpkSkZtN3lwT3dXSDVSSVBTM2lrL04xYllwSXdKL0NDTTIzQnlIRFhtaUU4elo0L3ptXG51MUxqMnc3ekZJd0x6dGFVZVlrNTB4cGxQWVk4Mkg5V2hiVHI2MGx1NThFckpESXJWcjNjZ0IxaitWV3Jkc2tEXG43ZWY5bDRoMW1ENVZUM3BjVmZVVG9PL2xZaXlBMG53VllsYlFSc0lYWjM3alNwVHVLQXA1eXU4dUVCTGp2V0RoXG52ZlM4L0NDamZCQ0xWWmMrSmdzQUVRPT1cbi0tLS0tRU5EIFBSSVZBVEUgS0VZLS0tLS1cbiIsCiAgImNsaWVudF9lbWFpbCI6ICJmaXJlYmFzZS1hZG1pbnNkay1seGFrN0BhdXRvd2F0ZXJwbGFudC0zZDc4ZC5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsCiAgImNsaWVudF9pZCI6ICIxMDY0MDA2NjE4NjcwNjM5MjMzMDUiLAogICJhdXRoX3VyaSI6ICJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20vby9vYXV0aDIvYXV0aCIsCiAgInRva2VuX3VyaSI6ICJodHRwczovL29hdXRoMi5nb29nbGVhcGlzLmNvbS90b2tlbiIsCiAgImF1dGhfcHJvdmlkZXJfeDUwOV9jZXJ0X3VybCI6ICJodHRwczovL3d3dy5nb29nbGVhcGlzLmNvbS9vYXV0aDIvdjEvY2VydHMiLAogICJjbGllbnRfeDUwOV9jZXJ0X3VybCI6ICJodHRwczovL3d3dy5nb29nbGVhcGlzLmNvbS9yb2JvdC92MS9tZXRhZGF0YS94NTA5L2ZpcmViYXNlLWFkbWluc2RrLWx4YWs3JTQwYXV0b3dhdGVycGxhbnQtM2Q3OGQuaWFtLmdzZXJ2aWNlYWNjb3VudC5jb20iLAogICJ1bml2ZXJzZV9kb21haW4iOiAiZ29vZ2xlYXBpcy5jb20iCn0K";
            String normalized = base64.trim();
            if ((normalized.startsWith("\"") && normalized.endsWith("\"")) ||
                (normalized.startsWith("'") && normalized.endsWith("'"))) {
                normalized = normalized.substring(1, normalized.length() - 1);
            }

            byte[] decoded = Base64.getDecoder().decode(normalized);
            String json = new String(decoded, StandardCharsets.UTF_8).trim();

            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = json.substring(1, json.length() - 1);
            }
            json = json.replace("\\n", "\n").replace("\\\"", "\"");

            if (!json.startsWith("{")) {
                throw new IllegalStateException("Decoded Firebase service account is not valid JSON object");
            }

            ByteArrayInputStream serviceAccountStream =
                    new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                firebaseApp = FirebaseApp.initializeApp(options);
            } else {
                firebaseApp = FirebaseApp.getInstance();
            }

            System.out.println("✅ Firebase initialized successfully with Realtime DB");
        } catch (Exception e) {
            System.err.println("❌ Firebase initialization failed: " + e.getMessage());
            throw new RuntimeException("Cannot initialize Firebase", e);
        }
    }

    public FirebaseDatabase getDatabase() {
        if (firebaseApp == null) {
            throw new IllegalStateException("Firebase app is not initialized");
        }
        return FirebaseDatabase.getInstance(firebaseApp);
    }

    public boolean isInitialized() {
        return firebaseApp != null;
    }
}
