package com.home.mytelegrambot.dao;

import com.home.mytelegrambot.dto.MemeSql;
import com.home.mytelegrambot.dto.ScheduledTask;
import com.home.mytelegrambot.dto.TaskType;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Repository
@Slf4j
public class MyRepository {
    private final NamedParameterJdbcTemplate jdbc;

    @Transactional
    public int[] getSalaryDays(String chatId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            return jdbc.queryForObject("SELECT first_day, second_day FROM memes.salary WHERE chat_id=:chat_id LIMIT 1", params, new DaysMapper());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new int[]{};
    }

    @Transactional
    public boolean saveSalaryDays(String chatId, int firstDay, int secondDay) {
        Long id = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            id = jdbc.queryForObject("SELECT id FROM memes.salary WHERE chat_id=:chat_id LIMIT 1", params, long.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("chat_id", chatId);
            params.put("first_day", firstDay);
            params.put("second_day", secondDay);

            SqlParameterSource paramSource = new MapSqlParameterSource(params);
            GeneratedKeyHolder holder = new GeneratedKeyHolder();
            if (id != null && id > 0) {
                jdbc.update("UPDATE memes.salary SET first_day=:first_day, second_day=:second_day WHERE id=:id", paramSource, holder);
            } else {
                jdbc.update("INSERT INTO memes.salary (chat_id, first_day, second_day) values (:chat_id, :first_day, :second_day)", paramSource, holder);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Transactional
    public boolean isPresentByLink(String link) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("link", link);
            Integer result = jdbc.queryForObject("SELECT COUNT(*) FROM memes.used WHERE link=:link LIMIT 1", params, Integer.class);
            log.info("result: {}", result);
            return result != null && result > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    @Transactional
    public boolean saveMemeLink(MemeSql memeSql) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("date", memeSql.getDate());
            params.put("link", memeSql.getLink());

            SqlParameterSource paramSource = new MapSqlParameterSource(params);
            GeneratedKeyHolder holder = new GeneratedKeyHolder();

            jdbc.update("INSERT INTO memes.used (date, link) values (:date, :link)", paramSource, holder);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Transactional
    public List<ScheduledTask> getScheduledTaskAll() {
        try {
            return jdbc.query("SELECT * FROM memes.scheduler", new ScheduledTaskMapper());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Transactional
    public List<ScheduledTask> getScheduledTaskByChatId(String chatId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            return jdbc.query("SELECT * FROM memes.scheduler WHERE chat_id=:chat_id", params, new ScheduledTaskMapper());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Transactional
    public boolean addScheduledTask(String chatId, TaskType type, int dayWeek, int hour, int minute) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            params.put("type", String.valueOf(type));
            params.put("day_week", dayWeek);
            params.put("hour", hour);
            params.put("minute", minute);

            SqlParameterSource paramSource = new MapSqlParameterSource(params);
            GeneratedKeyHolder holder = new GeneratedKeyHolder();

            jdbc.update("INSERT INTO memes.scheduler (chat_id, type, day_week, hour, minute) values (:chat_id, :type, :day_week, :hour, :minute)", paramSource, holder);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Transactional
    public boolean deleteScheduledTaskByChatIdAndTime(String chatId, TaskType type, int dayWeek, int hour, int minute) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            params.put("type", String.valueOf(type));
            params.put("day_week", dayWeek);
            params.put("hour", hour);
            params.put("minute", minute);
            jdbc.update("DELETE FROM memes.scheduler WHERE chat_id=:chat_id AND type=:type AND day_week=:day_week AND hour=:hour AND minute=:minute", params);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Transactional
    public boolean deleteScheduledTaskByChatId(String chatId, TaskType type) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("chat_id", chatId);
            params.put("type", String.valueOf(type));
            jdbc.update("DELETE FROM memes.scheduler WHERE chat_id=:chat_id AND type=:type", params);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }


}
