package com.home.mytelegrambot.service;

import com.home.mytelegrambot.config.TelegramBotConfig;
import com.home.mytelegrambot.dao.MyRepository;
import com.home.mytelegrambot.dto.MemeContent;
import com.home.mytelegrambot.dto.ScheduledTask;
import com.home.mytelegrambot.dto.TaskType;
import com.home.mytelegrambot.dto.TypeContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private TelegramBotConfig botConfig;
    private MemeService memeService;
    private SalaryService salaryService;
    private MyRepository myRepository;
    private MyScheduler myScheduler;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            String data = "";
            if (message.contains(" ")) {
                data = message.substring(message.indexOf(" ") + 1);
                message = message.substring(0, message.indexOf(" "));
            }
            String chatId = update.getMessage().getChatId().toString();
//            log.debug("chatId1: " + update.getMessage().getChatId().toString());
//            log.debug("chatId2: " + update.getMessage().getChat().getId());
//            log.debug("chatId3: " + update.getMessage().getChat().getLinkedChatId());

            switch (message) {
                case "/help":
                    sendText(chatId,
//                            "/start - включить бота" + "\n" +
//                                    "/stop - выключить бота" + "\n" +
                            "/dice - получить случайное число от 1 до 6" + "\n" +
                                    "/meme - получить мем" + "\n" +
                                    "/memeAdd {day week} {hour} {minute} - установить задание (день недели и время), когда бот будет присылать мемы. Дни недели ПН (0) - ВСК (6)." + "\n" +
                                    "/memeAddWeek {hour} {minute} - установить повторяющееся каждый день задание" + "\n" +
                                    "/memeList - получить список заданий день/время" + "\n" +
                                    "/memeRemove {} {day week} {hour} {minute} - удалить задание" + "\n" +
                                    "/memeRemoveAll - удалить все задания" + "\n" +
                                    "/setSalaryDays {first day} {second day} - установить для этого чата зарплатные дни" + "\n" +
                                    "/getSalaryDays - получить зарплатные дни для этого чата" + "\n" +
                                    "/salary - получить дни до зарплаты" + "\n" +
                                    "/help - список команд"
                    );
                    break;

                case "/start":
                    botConfig.setActive(true);
                    sendText(chatId, "Бот запущен =)");
                    break;

                case "/stop":
                    botConfig.setActive(false);
                    sendText(chatId, "Бот выключен =(");
                    break;

                case "/dice":
                    sendAnimatedDice(chatId);
                    break;

                case "/meme":
                    sendMemeOneChat(chatId);
                    break;

                case "/memeAdd":
                    memeAdd(chatId, data);
                    break;

                case "/memeAddWeek":
                    memeAddWeek(chatId, data);
                    break;

                case "/memeList":
                    memeList(chatId);
                    break;

                case "/memeRemove":
                    memeRemove(chatId, data);
                    break;

                case "/memeRemoveAll":
                    memeRemoveAll(chatId);
                    break;

                case "/setSalaryDays":
                    setSalaryDays(chatId, data);
                    break;

                case "/getSalaryDays":
                    getSalaryDays(chatId);
                    break;

                case "/salary":
                    sendDaysForSalary(chatId);
                    break;


            }
        }
    }

    private void setSalaryDays(String chatId, String data) {
        String[] days = data.split(" ");
        int firstDay = Integer.parseInt(days[0]);
        int secondDay = Integer.parseInt(days[1]);

        if (salaryService.setSalaryDays(chatId, firstDay, secondDay)) {
            sendText(chatId, "Для чата " + chatId + " установлены зарплатные дни " + firstDay + " и " + secondDay);
        } else {
            sendText(chatId, "Неверный ввод данных");
        }
    }

    private void getSalaryDays(String chatId) {
        int[] days = salaryService.getSalaryDays(chatId);
        if (days.length != 0) {
            sendText(chatId, "Для чата " + chatId + " установлены зарплатные дни " + days[0] + " и " + days[1]);
        } else {
            sendText(chatId, "Зарплатные дни для чата " + chatId + " не установлены");
        }
    }

    private void sendDaysForSalary(String chatId) {
        int[] days = salaryService.getSalaryDays(chatId);
        if (days.length != 0) {
            sendText(chatId, salaryService.getSalaryMessage(days[0], days[1]));
        } else {
            sendText(chatId, "Зарплатные дни для чата " + chatId + " не установлены");
        }
    }


    private void sendStaticDice(String chatId) {
        int value = (int) (Math.random() * 6 + 1);
        String message = "";
        switch (value) {
            case 1:
                message = "1\uFE0F⃣";
                break;
            case 2:
                message = "2\uFE0F⃣";
                break;
            case 3:
                message = "3\uFE0F⃣";
                break;
            case 4:
                message = "4\uFE0F⃣";
                break;
            case 5:
                message = "5\uFE0F⃣";
                break;
            case 6:
                message = "6\uFE0F⃣";
                break;
        }
        sendText(chatId, message);
    }


    private void sendMemeOneChat(String chatId) {
        MemeContent meme = memeService.getDayMeme();
        sendMemo(meme, chatId);
    }

    public void sendMemeGroupChats(List<String> chatsIdList) {
        if (!chatsIdList.isEmpty()) {
            MemeContent meme = memeService.getDayMeme();
            for (String chatId : chatsIdList) {
                sendMemo(meme, chatId);
            }
        }
    }

    public void sendMemo(MemeContent meme, String chatId) {
        if (meme != null) {
            switch (meme.getTypeContent()) {
                case TypeContent.IMAGE:
                    sendPhoto(chatId, meme.getLink(), meme.getCaption());
                    break;
                case TypeContent.VIDEO:
                    sendVideo(chatId, meme.getLink(), meme.getCaption());
                    break;
            }
        } else {
            log.debug("Мемы кончились(((");
//            for (String chatId : chats) {
//                bot.sendText(chatId, "Мемы кончились(((");
//            }
        }
    }

    public void memeAdd(String chatId, String data) {
        String[] dt = data.split(" ");
        int dayWeek = Integer.parseInt(dt[0]);
        int hour = Integer.parseInt(dt[1]);
        int minute = Integer.parseInt(dt[2]);

        if (dayWeek > 6 || dayWeek < 0 || hour > 23 || hour < 0 || minute > 60 || minute < 0) {
            sendText(chatId, "Неверные данные");
        } else {
            if (myRepository.addScheduledTask(chatId, TaskType.MEME, dayWeek, hour, minute)) {
                myScheduler.initScheduledTimeStamp();
                sendText(chatId, "ОК");
            }

        }
    }

    public void memeAddWeek(String chatId, String data) {
        String[] dt = data.split(" ");
        int hour = Integer.parseInt(dt[0]);
        int minute = Integer.parseInt(dt[1]);

        if (hour > 23 || hour < 0 || minute > 60 || minute < 0) {
            sendText(chatId, "Неверные данные");
        } else {
            boolean isGood = true;
            for (int dayWeek = 0; dayWeek < 7; dayWeek++) {
                isGood = isGood && myRepository.addScheduledTask(chatId, TaskType.MEME, dayWeek, hour, minute);
            }
            myScheduler.initScheduledTimeStamp();
            if (isGood) sendText(chatId, "ОК");
        }
    }

    public void memeList(String chatId) {
        List<ScheduledTask> list = myRepository.getScheduledTaskByChatId(chatId);

        if (!list.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (ScheduledTask task : list) {
                message.append("chatId: " + task.getChatId() + ", type: " + task.getType() + ", day_week: " + task.getDayWeek() + ", hour: " + task.getHour() + ", minute: " + task.getMinute() + "\n");
            }
            sendText(chatId, message.substring(0, message.length() - 1));
        } else {
            sendText(chatId, "Список пуст");
        }
    }

    public void memeRemove(String chatId, String data) {
        String[] dt = data.split(" ");
        int dayWeek = Integer.parseInt(dt[0]);
        int hour = Integer.parseInt(dt[1]);
        int minute = Integer.parseInt(dt[2]);

        if (myRepository.deleteScheduledTaskByChatIdAndTime(chatId, TaskType.MEME, dayWeek, hour, minute)) {
            myScheduler.initScheduledTimeStamp();
            sendText(chatId, "OK");
        } else {
            sendText(chatId, "Неверные данные");
        }
    }

    public void memeRemoveAll(String chatId) {
        if (myRepository.deleteScheduledTaskByChatId(chatId, TaskType.MEME)) {
            myScheduler.initScheduledTimeStamp();
            sendText(chatId, "OK");
        } else {
            sendText(chatId, "Неверные данные");
        }
    }


    public void sendDaysForSalary(List<String> chatsIdList) {
        for (String chatId : chatsIdList) {
            int[] days = salaryService.getSalaryDays(chatId);
            if (days.length != 0) {
                sendText(chatId, salaryService.getSalaryMessage(days[0], days[1]));
            } else {
                sendText(chatId, "Дни не установлены...");
            }
        }
    }


    private void sendText(String chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendPhoto(String chatId, String link, String caption) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setPhoto(new InputFile(link));

        if (!caption.isEmpty()) sendPhoto.setCaption(caption);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendVideo(String chatId, String link, String caption) {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(chatId);
        sendVideo.setVideo(new InputFile(link));
        sendVideo.setHasSpoiler(true);

        if (!caption.isEmpty()) sendVideo.setCaption(caption);
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendAnimatedDice(String chatId) {
// if api not supported
//        int value = (int) (Math.random() * 6 + 1);
//        SendAnimation sendAnimation = new SendAnimation();
//        sendAnimation.setChatId(chatId);
//        sendAnimation.setAnimation(new InputFile(new File(getClass().getClassLoader().getResource("dice_" + value + ".gif").getFile())));
//        try {
//            execute(sendAnimation);
//        } catch (TelegramApiException e) {
//            log.error(e.getMessage());
//        }


        SendDice sendDice = new SendDice();
        sendDice.setChatId(chatId);
        try {
            execute(sendDice);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
