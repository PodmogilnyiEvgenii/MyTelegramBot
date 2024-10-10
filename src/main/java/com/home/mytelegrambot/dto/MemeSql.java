package com.home.mytelegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class MemeSql {
    @Id
    private long id;
    private Timestamp date;
    private String link;
}
