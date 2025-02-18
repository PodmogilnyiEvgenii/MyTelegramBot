package com.home.mytelegrambot.service;

import com.home.mytelegrambot.config.TelegramBotConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("${application.endpoint.root}")
@AllArgsConstructor
@Slf4j
public class RestController {
    TelegramBotConfig botConfig;

    @RequestMapping("${application.endpoint.status}")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("OK");
    }

    @RequestMapping("${application.endpoint.status}")
    public ResponseEntity<String> getExtendedStatus() {
        return ResponseEntity.ok(getTelegramBotStatus());
    }

    private String getTelegramBotStatus() {
        String apiUrl = "https://api.telegram.org/bot" + botConfig.getToken() + "/getMe";

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return response.statusCode() + "";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return "Error";
    }
}
