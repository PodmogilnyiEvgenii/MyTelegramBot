package com.home.mytelegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MemeContent {
    private String link;
    private TypeContent typeContent;
    private String caption;
}
