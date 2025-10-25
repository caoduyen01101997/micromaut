package com.example;

import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {
        System.out.println("🚀 Starting Micronaut application...");
        Micronaut.run(Application.class, args);
        System.out.println("✅ Micronaut application started!");
    }
}
