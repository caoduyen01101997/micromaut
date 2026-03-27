package com.example.service;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Singleton;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.annotation.PostConstruct; // Thay vì javax.annotation.PostConstruct
import jakarta.annotation.Nullable;

import java.util.ArrayList;

@Singleton
public class RemindService {
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final @Nullable PumpService pumpService;
    
    // Hardcode values thay vì System.getenv()
    private final String notionToken = "ntn_Y39296702863DRjCjOBCg18Ox9MVSuqSMEZ4PLPWX8Y3Hi";
    private final String databaseId = "2900ea3a028e8054abb5f520e01f89fe";
    private final String telegramToken = "7500392061:AAHA5S6h69-XJ9FTWIOUnLMqINRhKy7FBWk";
    private final String telegramChatId = "890385679";
    
    private long lastUpdateId = 0; // Lưu ID của update cuối cùng

    // Constructor để log khi bean được tạo
    public RemindService(@Nullable PumpService pumpService) {
        this.pumpService = pumpService;
        System.out.println("🚀 RemindService bean created at: " + LocalDateTime.now() + (pumpService != null ? " with PumpService" : " WITHOUT PumpService (Firebase unavailable)"));
        System.out.println("📱 Chat ID: " + telegramChatId);
        System.out.println("🤖 Bot Token: " + telegramToken.substring(0, 10) + "...");
    }

    @Scheduled(cron = "0 0 8 * * *") // 08:00 mỗi ngày
    void fetchAndNotifyDaily() {
        System.out.println("[" + LocalDateTime.now() + "] Running scheduled reminder at 8:00 AM");
        sendReminder();
    }

    // Test connection khi khởi động và reset offset
    @Scheduled(initialDelay = "5s")
    void testBotConnection() {
        System.out.println("[" + LocalDateTime.now() + "] Testing bot connection...");
        try {
            // Đầu tiên, reset offset bằng cách lấy tất cả update cũ
            String telegramUrl = "https://api.telegram.org/bot" + telegramToken + "/getUpdates";
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(telegramUrl))
                    .GET()
                    .build();

            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonNode updates = mapper.readTree(response.body());
                JsonNode result = updates.path("result");
                
                // Tìm update ID cao nhất để set làm offset
                long maxUpdateId = 0;
                for (JsonNode update : result) {
                    long updateId = update.path("update_id").asLong();
                    maxUpdateId = Math.max(maxUpdateId, updateId);
                }
                
                if (maxUpdateId > 0) {
                    lastUpdateId = maxUpdateId;
                    System.out.println("🔄 Reset lastUpdateId to: " + lastUpdateId);
                    
                    // Clear tất cả pending updates
                    String clearUrl = "https://api.telegram.org/bot" + telegramToken + "/getUpdates?offset=" + (lastUpdateId + 1);
                    HttpRequest clearReq = HttpRequest.newBuilder().uri(URI.create(clearUrl)).GET().build();
                    HTTP.send(clearReq, HttpResponse.BodyHandlers.ofString());
                }
                
                // Test bot info
                String botInfoUrl = "https://api.telegram.org/bot" + telegramToken + "/getMe";
                HttpRequest botReq = HttpRequest.newBuilder().uri(URI.create(botInfoUrl)).GET().build();
                HttpResponse<String> botRes = HTTP.send(botReq, HttpResponse.BodyHandlers.ofString());
                
                if (botRes.statusCode() == 200) {
                    JsonNode botInfo = mapper.readTree(botRes.body());
                    String botName = botInfo.path("result").path("username").asText();
                    System.out.println("✅ Bot connection successful! Bot username: @" + botName);
                    
                    // Send test message
                    sendTelegramMessage("🤖 Bot đã khởi động thành công!\nThời gian: " + LocalDateTime.now() + "\nSẵn sàng nhận lệnh mới!");
                }
            } else {
                System.err.println("❌ Bot connection failed: " + response.statusCode());
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("❌ Error testing bot connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Polling Telegram messages mỗi 5 giây để kiểm tra command
    @Scheduled(fixedDelay = "5s")
    void pollTelegramMessages() {
        System.out.println("🔍 [" + LocalDateTime.now() + "] Polling Telegram messages... lastUpdateId: " + lastUpdateId);
        
        try {
            String telegramUrl = "https://api.telegram.org/bot" + telegramToken + "/getUpdates?offset=" + (lastUpdateId + 1) + "&timeout=3&limit=10";
            
            System.out.println("📡 Calling URL: " + telegramUrl.replace(telegramToken, "***TOKEN***"));
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(telegramUrl))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("📊 Telegram response status: " + response.statusCode());
            
            if (response.statusCode() == 200) {
                JsonNode updates = mapper.readTree(response.body());
                JsonNode result = updates.path("result");
                
                System.out.println("📦 Number of updates: " + result.size());
                System.out.println("📝 Current lastUpdateId: " + lastUpdateId);
                
                if (result.isArray() && result.size() > 0) {
                    System.out.println("🆕 NEW MESSAGES FOUND!");
                    // In ra một phần response để debug
                    System.out.println("📄 First few chars: " + response.body().substring(0, Math.min(200, response.body().length())) + "...");
                }
                
                for (JsonNode update : result) {
                    long updateId = update.path("update_id").asLong();
                    System.out.println("🔄 Processing update ID: " + updateId + " (current lastUpdateId: " + lastUpdateId + ")");
                    
                    lastUpdateId = Math.max(lastUpdateId, updateId);
                    
                    JsonNode message = update.path("message");
                    if (message.has("text")) {
                        String text = message.path("text").asText();
                        String chatId = message.path("chat").path("id").asText();
                        String firstName = message.path("from").path("first_name").asText("");
                        
                        System.out.println("💬 Received message: '" + text + "' from chat: " + chatId + " (user: " + firstName + ")");
                        System.out.println("🎯 Expected chat ID: " + telegramChatId);
                        
                        // Kiểm tra command từ đúng chat ID
                        if (telegramChatId.equals(chatId)) {
                            System.out.println("✅ Chat ID matches, processing command...");
                            handleCommand(text, firstName);
                        } else {
                            System.out.println("❌ Chat ID does not match! Received: " + chatId + ", Expected: " + telegramChatId);
                        }
                    } else {
                        System.out.println("⚠️ Update does not contain text message");
                        System.out.println("📋 Update content: " + update.toString());
                    }
                }
                
                System.out.println("🔚 Updated lastUpdateId to: " + lastUpdateId);
            } else {
                System.err.println("❌ Telegram API error: " + response.statusCode());
                System.err.println("📄 Response body: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("💥 [" + LocalDateTime.now() + "] Error polling Telegram messages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleCommand(String command, String userName) {
        System.out.println("🎮 [" + LocalDateTime.now() + "] Handling command: '" + command + "' from user: " + userName);
        
        switch (command.toLowerCase().trim()) {
            case "/remind":
                System.out.println("📋 Executing /remind command");
                sendReminder();
                break;
            case "/help":
                System.out.println("❓ Executing /help command");
                sendTelegramMessage("🤖 Bot Commands:\n/remind - Xem công việc hôm nay\n/help - Hiển thị trợ giúp\n/test - Test bot\n/ping - Ping bot\n/pump_on - Bật máy bơm\n/pump_off - Tắt máy bơm\n/pump_status - Xem trạng thái máy bơm");
                break;
            case "/start":
                System.out.println("🏁 Executing /start command");
                sendTelegramMessage("👋 Chào " + userName + "!\nBot sẽ nhắc nhở công việc hàng ngày lúc 8:00 AM.\nGõ /help để xem các lệnh có sẵn.");
                break;
            case "/test":
                System.out.println("🧪 Executing /test command");
                sendTelegramMessage("🤖 Bot đang hoạt động bình thường!\nThời gian: " + LocalDateTime.now());
                break;
            case "/ping":
                System.out.println("🏓 Executing /ping command");
                sendTelegramMessage("🏓 Pong! Bot đang online.\nChat ID: " + telegramChatId + "\nTime: " + LocalDateTime.now());
                break;
            case "/pump_on":
                System.out.println("💧 Executing /pump_on command");
                if (pumpService == null) {
                    sendTelegramMessage("⚠️ Máy bơm không khả dụng (Firebase offline)");
                } else {
                    try {
                        pumpService.turnOn();
                        sendTelegramMessage("✅ Máy bơm đã được BẬT.");
                    } catch (Exception e) {
                        sendTelegramMessage("❌ Không thể bật máy bơm: " + e.getMessage());
                    }
                }
                break;
            case "/pump_off":
                System.out.println("🛑 Executing /pump_off command");
                if (pumpService == null) {
                    sendTelegramMessage("⚠️ Máy bơm không khả dụng (Firebase offline)");
                } else {
                    try {
                        pumpService.turnOff();
                        sendTelegramMessage("✅ Máy bơm đã được TẮT.");
                    } catch (Exception e) {
                        sendTelegramMessage("❌ Không thể tắt máy bơm: " + e.getMessage());
                    }
                }
                break;
            case "/pump_status":
            case "/pumpstatus":
                System.out.println("📊 Executing /pump_status (/pumpStatus) command");
                if (pumpService == null) {
                    sendTelegramMessage("⚠️ Máy bơm không khả dụng (Firebase offline)");
                } else {
                    try {
                        boolean isOn = pumpService.getStatus();
                        sendTelegramMessage("📊 Trạng thái máy bơm hiện tại: " + (isOn ? "🟢 ĐANG BẬT" : "🔴 ĐANG TẮT"));
                    } catch (Exception e) {
                        sendTelegramMessage("❌ Không thể lấy trạng thái máy bơm: " + e.getMessage());
                    }
                }
                break;
            default:
                System.out.println("❓ Unknown command: '" + command + "'");
                sendTelegramMessage("❓ Lệnh không hợp lệ: " + command + "\nGõ /help để xem danh sách lệnh.");
                break;
        }
    }

    private void sendReminder() {
        System.out.println("[" + LocalDateTime.now() + "] Starting sendReminder()");
        LocalDate today = LocalDate.now();
        
        // ✅ Đúng rồi - không có filter, lấy tất cả
        String filterJson = "{}";
        
        try {
            System.out.println("📋 Querying Notion database without filter...");
            
            HttpRequest notionReq = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.notion.com/v1/databases/" + databaseId + "/query"))
                    .header("Authorization", "Bearer " + notionToken)
                    .header("Notion-Version", "2022-06-28")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(filterJson))
                    .build();

            HttpResponse<String> notionRes = HTTP.send(notionReq, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("📊 Notion response status: " + notionRes.statusCode());
            
            if (notionRes.statusCode() >= 300) {
                System.err.println("❌ Notion error: " + notionRes.statusCode() + " " + notionRes.body());
                sendTelegramMessage("❌ Lỗi kết nối Notion: " + notionRes.statusCode());
                return;
            }

            // Parse JSON response
            JsonNode root = mapper.readTree(notionRes.body());
            List<String> tasks = new ArrayList<>();
            
            System.out.println("📦 Found " + root.path("results").size() + " total records");

            for (JsonNode page : root.path("results")) {
                JsonNode props = page.path("properties");

                // Lấy task name
                JsonNode titleArr = props.path("Task name").path("title");
                if (titleArr.isArray() && titleArr.size() > 0) {
                    String taskName = titleArr.get(0).path("plain_text").asText();
                    
                    // Lấy thêm thông tin status
                    String status = props.path("Status").path("status").path("name").asText("No Status");
                    
                    // Lấy due date
                    String dueDate = props.path("Due date").path("date").path("start").asText("No Date");
                    
                    // Format task với thông tin
                    String taskInfo = taskName + " [" + status + " - Due: " + dueDate + "]";
                    tasks.add(taskInfo);
                    
                    System.out.println("📋 Task: " + taskInfo);
                }
            }

            // Tạo message
            String message;
            if (tasks.isEmpty()) {
                message = "📅 Không có task nào trong database! 😌";
            } else {
                StringBuilder sb = new StringBuilder("📋 Tất cả tasks trong Notion:\n\n");
                for (int i = 0; i < tasks.size(); i++) {
                    sb.append("✅ ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
                }
                sb.append("\n💪 Tổng cộng: " + tasks.size() + " tasks!");
                message = sb.toString();
            }

            sendTelegramMessage(message);

        } catch (Exception e) {
            System.err.println("[" + LocalDateTime.now() + "] Reminder error: " + e.getMessage());
            e.printStackTrace();
            sendTelegramMessage("❌ Có lỗi xảy ra: " + e.getMessage());
        }
    }
    
    private void sendTelegramMessage(String message) {
        System.out.println("📤 [" + LocalDateTime.now() + "] Sending message to Telegram...");
        try {
            String telegramUrl = "https://api.telegram.org/bot" + telegramToken + "/sendMessage";
            String bodyJson = "{\"chat_id\":\"" + telegramChatId + "\",\"text\":\"" + escape(message) + "\"}";
            
            HttpRequest tgReq = HttpRequest.newBuilder()
                    .uri(URI.create(telegramUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();

            HttpResponse<String> tgRes = HTTP.send(tgReq, HttpResponse.BodyHandlers.ofString());
            if (tgRes.statusCode() >= 300) {
                System.err.println("Telegram error: " + tgRes.statusCode() + " " + tgRes.body());
            } else {
                System.out.println("✅ [" + LocalDateTime.now() + "] Message sent successfully to Telegram!");
            }
        } catch (Exception e) {
            System.err.println("[" + LocalDateTime.now() + "] Error sending telegram message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }


    @PostConstruct
    public void testNotionConnection() {
    System.out.println("🧪 Testing Notion connection...");
    try {
        // Test database access
        String url = "https://api.notion.com/v1/databases/" + databaseId;
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + notionToken)
                .header("Notion-Version", "2022-06-28")
                .GET()
                .build();

        HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("📊 Notion test status: " + response.statusCode());
        
        if (response.statusCode() == 200) {
            JsonNode database = mapper.readTree(response.body());
            JsonNode titleArray = database.path("title");
            String title = "Unknown";
            if (titleArray.isArray() && titleArray.size() > 0) {
                title = titleArray.get(0).path("plain_text").asText();
            }
            
            System.out.println("✅ Notion connection successful! Database: " + title);
            
            // Show properties
            JsonNode properties = database.path("properties");
            System.out.println("📝 Available properties:");
            properties.fieldNames().forEachRemaining(fieldName -> {
                JsonNode property = properties.path(fieldName);
                String type = property.path("type").asText();
                System.out.println("  - " + fieldName + " (" + type + ")");
            });
            
        } else {
            System.err.println("❌ Notion connection failed: " + response.statusCode());
            System.err.println("Response: " + response.body());
        }
    } catch (Exception e) {
        System.err.println("💥 Notion test error: " + e.getMessage());
    }
}

    public void checkTelegramManual() {
    System.out.println("🔍 Manual check Telegram messages...");
    try {
        String telegramUrl = "https://api.telegram.org/bot" + telegramToken + "/getUpdates?limit=5";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(telegramUrl))
                .GET()
                .build();

        HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("📊 Manual check response status: " + response.statusCode());
        
        if (response.statusCode() == 200) {
            JsonNode updates = mapper.readTree(response.body());
            JsonNode result = updates.path("result");
            
            System.out.println("📦 Manual check - Number of messages: " + result.size());
            
            if (result.size() > 0) {
                System.out.println("📄 Last 5 messages:");
                for (int i = 0; i < Math.min(5, result.size()); i++) {
                    JsonNode update = result.get(i);
                    JsonNode message = update.path("message");
                    if (message.has("text")) {
                        String text = message.path("text").asText();
                        String chatId = message.path("chat").path("id").asText();
                        long updateId = update.path("update_id").asLong();
                        System.out.println("  " + (i+1) + ". UpdateID: " + updateId + ", Chat: " + chatId + ", Text: '" + text + "'");
                    }
                }
            }
        }
    } catch (Exception e) {
        System.err.println("💥 Manual check error: " + e.getMessage());
        e.printStackTrace();
    }
    }
}

