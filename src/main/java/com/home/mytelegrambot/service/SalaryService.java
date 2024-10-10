package com.home.mytelegrambot.service;

import com.home.mytelegrambot.dao.MyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
@AllArgsConstructor
public class SalaryService {
    private MyRepository myRepository;

    public boolean setSalaryDays(String chatId, int firstDay, int secondDay) {
        if (firstDay < 1 || 28 < firstDay || secondDay < 1 || 28 < secondDay || secondDay < firstDay) {
            return false;
        }
        return myRepository.saveSalaryDays(chatId, firstDay, secondDay);
    }

    public int[] getSalaryDays(String chatId) {
        return myRepository.getSalaryDays(chatId);
    }

    private int getDaysForSalary(int firstDay, int secondDay) {

        //@TODO сделать сдвиг на пятницу если день приходится на выходные
        Calendar calendarNow = Calendar.getInstance();
        int ans = 0;
        List<Calendar> list = new ArrayList<>();

        Calendar calendarFirst = Calendar.getInstance();
        calendarFirst.set(Calendar.DAY_OF_MONTH, firstDay);
        list.add(calendarFirst);

        Calendar calendarSecond = Calendar.getInstance();
        calendarSecond.set(Calendar.DAY_OF_MONTH, secondDay);
        list.add(calendarSecond);

        Calendar calendarNextFirst = Calendar.getInstance();
        calendarNextFirst.set(Calendar.DAY_OF_MONTH, firstDay);

        if (calendarNow.get(Calendar.MONTH) < 11) {
            calendarNextFirst.set(Calendar.MONTH, calendarNow.get(Calendar.MONTH) + 1);
        } else {
            calendarNextFirst.set(Calendar.MONTH, 0);
            calendarNextFirst.set(Calendar.YEAR, calendarNow.get(Calendar.YEAR) + 1);
        }
        list.add(calendarNextFirst);

        for (Calendar calendar : list) {

            ans = (int) Duration.between(calendarNow.getTime().toInstant(), calendar.getTime().toInstant()).toDays();

            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                ans = ans - 1;
            } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                ans = ans - 2;
            }

            if (ans >= 0) return ans;
        }

        return -1;
    }

    public String getSalaryMessage(int firstDay, int secondDay) {
        int daysForSalary = getDaysForSalary(firstDay, secondDay);
        return switch (daysForSalary) {
            case 0 -> "Сегодня зарплата!";
            case 1 -> "До получения зарплаты осталось " + daysForSalary + " день";
            case 2, 3, 4 -> "До получения зарплаты осталось " + daysForSalary + " дня";
            default -> "До получения зарплаты осталось " + daysForSalary + " дней";
        };
    }
}
