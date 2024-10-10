package com.home.mytelegrambot.dao;

import com.home.mytelegrambot.dto.ScheduledTask;
import com.home.mytelegrambot.dto.TaskType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduledTaskMapper implements RowMapper<ScheduledTask> {
    @Override
    public ScheduledTask mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id = rs.getLong("id");
        String chatId = rs.getString("chat_id");
        TaskType type = TaskType.valueOf(rs.getString("type"));
        int dayWeek = rs.getInt("day_week");
        int hour = rs.getInt("hour");
        int minute = rs.getInt("minute");
        return new ScheduledTask(id, chatId, type, dayWeek, hour, minute);
    }
}
