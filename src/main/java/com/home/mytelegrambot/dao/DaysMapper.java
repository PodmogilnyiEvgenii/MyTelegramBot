package com.home.mytelegrambot.dao;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DaysMapper implements RowMapper<int[]> {
    @Override
    public int[] mapRow(ResultSet rs, int rowNum) throws SQLException {
        int firstDay = rs.getInt("first_day");
        int secondDay = rs.getInt("second_day");

        if (firstDay == 0 || secondDay == 0) {
            return new int[]{};
        }

        return new int[] {firstDay, secondDay};
    }
}
