package com.example.controller;

import com.example.service.PumpService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.annotation.Nullable;

import java.util.Map;

@Controller("/pump")
public class PumpController {

    private final @Nullable PumpService pumpService;

    public PumpController(@Nullable PumpService pumpService) {
        this.pumpService = pumpService;
    }

    @Post("/on")
    public HttpResponse<Map<String, Object>> turnOn() {
        if (pumpService == null) {
            return HttpResponse.serverError(Map.of("success", false, "message", "Firebase not available"));
        }
        try {
            pumpService.turnOn();
            return HttpResponse.ok(Map.of("success", true, "message", "Pump turned ON", "state", "ON"));
        } catch (Exception e) {
            return HttpResponse.serverError(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @Post("/off")
    public HttpResponse<Map<String, Object>> turnOff() {
        if (pumpService == null) {
            return HttpResponse.serverError(Map.of("success", false, "message", "Firebase not available"));
        }
        try {
            pumpService.turnOff();
            return HttpResponse.ok(Map.of("success", true, "message", "Pump turned OFF", "state", "OFF"));
        } catch (Exception e) {
            return HttpResponse.serverError(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @Get("/status")
    public HttpResponse<Map<String, Object>> getStatus() {
        if (pumpService == null) {
            return HttpResponse.serverError(Map.of("success", false, "message", "Firebase not available"));
        }
        try {
            boolean isOn = pumpService.getStatus();
            return HttpResponse.ok(Map.of("success", true, "state", isOn ? "ON" : "OFF", "isOn", isOn));
        } catch (Exception e) {
            return HttpResponse.serverError(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
