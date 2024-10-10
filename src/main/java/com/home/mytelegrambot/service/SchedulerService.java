package com.home.mytelegrambot.service;

import com.home.mytelegrambot.config.TelegramBotConfig;
import com.home.mytelegrambot.dto.TaskType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.Calendar;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class SchedulerService {
    private TelegramBot bot;
    private TelegramBotConfig botConfig;
    private MyScheduler myScheduler;

    //@Scheduled(fixedRate = 60000)
    public void sendDaysForSalary() {
        if (botConfig.isActive()) {
            //log.debug("Check count days for salary");
            //Calendar calendar = Calendar.getInstance();

            List<String> chartIdList = myScheduler.getListForThisTime(TaskType.SALARY);
            bot.sendDaysForSalary(chartIdList);

//            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY &&
//                    calendar.get(Calendar.AM_PM) == Calendar.AM && calendar.get(Calendar.HOUR) == 10 && calendar.get(Calendar.MINUTE) == 0) {
//
//            }
        }
    }

    @Scheduled(fixedRate = 60000, initialDelay = 1000)
    public void sendDayMemo() {
        if (botConfig.isActive()) {
            //log.debug("Check it's meme time");

            List<String> chartIdList = myScheduler.getListForThisTime(TaskType.MEME);
            bot.sendMemeGroupChats(chartIdList);

//            if ((calendar.get(Calendar.AM_PM) == Calendar.AM && calendar.get(Calendar.HOUR) == 10 && calendar.get(Calendar.MINUTE) == 0) ||
//                    (calendar.get(Calendar.AM_PM) == Calendar.PM && calendar.get(Calendar.HOUR) == 6 && calendar.get(Calendar.MINUTE) == 0)
//            ) {
//                bot.sendMemeGroupChats();
//            }
        }
    }


}
