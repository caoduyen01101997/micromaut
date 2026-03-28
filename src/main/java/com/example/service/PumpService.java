package com.example.service;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import jakarta.inject.Singleton;
import jakarta.annotation.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Singleton
public class PumpService {

    private static final String PUMP_STATUS_PATH = "/pump/state";

    private final @Nullable FirebaseRealtimeService firebaseRealtimeService;

    public PumpService(@Nullable FirebaseRealtimeService firebaseRealtimeService) {
        this.firebaseRealtimeService = firebaseRealtimeService;
    }

    public void turnOn() {
        setPumpStatus(true);
    }

    public void turnOff() {
        setPumpStatus(false);
    }

    public boolean getStatus() {
        if (firebaseRealtimeService == null || !firebaseRealtimeService.isInitialized()) {
            System.out.println("⚠️ Firebase not available, returning pump OFF");
            return false;
        }
        try {
            FirebaseDatabase database = firebaseRealtimeService.getDatabase();
            DatabaseReference ref = database.getReference(PUMP_STATUS_PATH);

            CompletableFuture<Boolean> future = new CompletableFuture<>();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Object raw = snapshot.getValue();
                    System.out.println("🔍 Firebase raw value: " + raw
                            + " (type: " + (raw == null ? "null" : raw.getClass().getSimpleName()) + ")");
                    if (raw instanceof Boolean) {
                        future.complete((Boolean) raw);
                    } else if (raw instanceof String) {
                        future.complete("ON".equalsIgnoreCase((String) raw));
                    } else {
                        future.complete(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    future.completeExceptionally(new RuntimeException(error.getMessage()));
                }
            });

            return future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("❌ Cannot read pump status: " + e.getMessage());
            return false;
        }
    }

    private void setPumpStatus(boolean status) {
        if (firebaseRealtimeService == null || !firebaseRealtimeService.isInitialized()) {
            System.out.println("⚠️ Firebase not available, skipping pump " + (status ? "ON" : "OFF"));
            return;
        }
        String state = status ? "ON" : "OFF";
        try {
            FirebaseDatabase database = firebaseRealtimeService.getDatabase();
            DatabaseReference ref = database.getReference(PUMP_STATUS_PATH);

            CompletableFuture<Void> future = new CompletableFuture<>();
            ref.setValue(state, (error, dbRef) -> {
                if (error != null) {
                    future.completeExceptionally(new RuntimeException(error.getMessage()));
                } else {
                    future.complete(null);
                }
            });

            future.get(10, TimeUnit.SECONDS);
            System.out.println("✅ Pump status set to: " + state + " at path: " + PUMP_STATUS_PATH);
        } catch (Exception e) {
            System.err.println("❌ Cannot update pump status: " + e.getMessage());
        }
    }
}
