package com.home.mytelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MyTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTelegramBotApplication.class, args);
    }

}


//docker-compose up --build
//
