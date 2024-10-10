package com.home.mytelegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Data
public class ScheduledTask {
    @Id
    private long id;
    private String chatId;
    private TaskType type;
    private int dayWeek;
    private int hour;
    private int minute;

}
