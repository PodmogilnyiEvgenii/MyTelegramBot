package com.home.mytelegrambot.service;

import com.home.mytelegrambot.dao.MyRepository;
import com.home.mytelegrambot.dto.ScheduledTask;
import com.home.mytelegrambot.dto.TaskType;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class MyScheduler {
    private MyRepository myRepository;
    private List<ScheduledTask> salarySchedulerList;
    private List<ScheduledTask> memesSchedulerList;

    @PostConstruct
    public void initScheduledTimeStamp() {
        salarySchedulerList = new ArrayList<>();
        memesSchedulerList = new ArrayList<>();
        
        List<ScheduledTask> list = myRepository.getScheduledTaskAll();

        for (ScheduledTask scheduledTask : list) {
            if (scheduledTask.getType() == TaskType.MEME) {
                memesSchedulerList.add(scheduledTask);
            } else if (scheduledTask.getType() == TaskType.SALARY) {
                salarySchedulerList.add(scheduledTask);
            }
        }

        log.info("Meme scheduled tasks size: {}", memesSchedulerList.size());
        log.info("Salary scheduled tasks size: {}", salarySchedulerList.size());
    }

    public List<String> getListForThisTime(TaskType type) {
        Calendar calendar = Calendar.getInstance();
        List<String> list = new ArrayList<>();

        switch (type) {
            case MEME:
                list = getIdsFromList(memesSchedulerList, calendar);
                break;
            case SALARY:
                list = getIdsFromList(salarySchedulerList, calendar);
                break;
        }
        return list;
    }

    private List<String> getIdsFromList(List<ScheduledTask> list, Calendar calendar) {
        List<String> chatsIdList = new ArrayList<>();
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (dayWeek < 0) dayWeek += 7;
        int hour = calendar.get(Calendar.AM_PM) == Calendar.AM ? calendar.get(Calendar.HOUR) : calendar.get(Calendar.HOUR) + 12;
        int minute = calendar.get(Calendar.MINUTE);

        for (ScheduledTask scheduledTask : list) {
            if (scheduledTask.getDayWeek() == dayWeek && scheduledTask.getHour() == hour && scheduledTask.getMinute() == minute) {
                chatsIdList.add(scheduledTask.getChatId());
            }
        }

        return chatsIdList;
    }

}
