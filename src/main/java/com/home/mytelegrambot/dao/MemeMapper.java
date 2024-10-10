package com.home.mytelegrambot.dao;

import com.home.mytelegrambot.dto.MemeSql;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MemeMapper implements RowMapper<MemeSql> {

    @Override
    public MemeSql mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        Timestamp date = rs.getTimestamp("date");
        String link = rs.getString("link");
        return new MemeSql(id, date, link);
    }
}
