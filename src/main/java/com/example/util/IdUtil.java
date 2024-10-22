package com.example.util;

import java.util.UUID;

public class IdUtil {
    public static  Long generateId() {
        // Generate a random UUID
        UUID uuid = UUID.randomUUID();

        // Convert the UUID to a string and remove the hyphens
        String uuidStr = uuid.toString().replace("-", "");

        // Convert the first 15 characters of the UUID to a long
        Long id = Long.parseLong(uuidStr.substring(0, 10), 16);

        return id;
    }
}
