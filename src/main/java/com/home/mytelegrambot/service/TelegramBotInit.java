package com.home.mytelegrambot.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.GetMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class TelegramBotInit {
    private final TelegramBot bot;


    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);

            List<BotCommand> botCommandList = new ArrayList<>();
            botCommandList.add(new BotCommand("/help","получить список команд"));
            botCommandList.add(new BotCommand("/dice","получить случайное число от 1 до 6"));
            //botCommandList.add(new BotCommand("/stDice","получить случайное число от 1 до 6"));
            botCommandList.add(new BotCommand("/meme","получить мем"));
            botCommandList.add(new BotCommand("/salary","получить сколько осталось дней до зарплаты"));

            bot.execute(new SetMyCommands(botCommandList, null, "ru"));

        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

//help - получить список команд
//dice - получить случайное число от 1 до 6
//meme - получить мем
//salary - получить сколько осталось дней до зарплаты
