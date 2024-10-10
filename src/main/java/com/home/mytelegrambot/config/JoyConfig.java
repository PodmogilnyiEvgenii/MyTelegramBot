package com.home.mytelegrambot.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@Data
public class JoyConfig {
    private Map<Integer, List<String>> linkMap;
    private Map<Integer, String> captionMap;
    private String userAgent = "Chrome/4.0.249.0 Safari/532.5";
    private String referrer = "http://www.google.com";

    public JoyConfig() {
        List<String> urlMondayList = new ArrayList<>();
        urlMondayList.add("https://old.reactor.cc/tag/%D0%BF%D0%BE%D0%BD%D0%B5%D0%B4%D0%B5%D0%BB%D1%8C%D0%BD%D0%B8%D0%BA");  //понедельник

        List<String> urlTuesdayList = new ArrayList<>();
        urlTuesdayList.add("https://old.reactor.cc/tag/%D1%85%D0%BE%D1%80%D0%BE%D1%88%D0%B8%D0%B9+%D0%BC%D0%B0%D0%BB%D1%8C%D1%87%D0%B8%D0%BA"); //хороший мальчик
        urlTuesdayList.add("https://old.reactor.cc/tag/%D1%88%D0%B5%D1%80%D1%81%D1%82%D1%8F%D0%BD%D0%BE%D0%B9+%D0%BF%D0%B8%D0%B4%D0%BE%D1%80%D0%B0%D1%81"); //шерстяной пидорас
        urlTuesdayList.add("https://old.reactor.cc/tag/%D0%B0%D0%B7%D0%B8%D0%B0%D1%82%D1%81%D0%BA%D0%B8%D0%B5+%D0%B2%D1%8B%D0%B4%D1%83%D0%BC%D1%89%D0%B8%D0%BA%D0%B8"); //азиатские выдумщики

        List<String> urlWednesdayList = new ArrayList<>();
        urlWednesdayList.add("https://old.reactor.cc/tag/It+is+Wednesday+My+Dudes");

        List<String> urlThursdayList = new ArrayList<>();
        urlThursdayList.add("https://old.reactor.cc/tag/%D1%85%D0%BE%D1%80%D0%BE%D1%88%D0%B8%D0%B9+%D0%BC%D0%B0%D0%BB%D1%8C%D1%87%D0%B8%D0%BA"); //хороший мальчик
        urlThursdayList.add("https://old.reactor.cc/tag/%D1%88%D0%B5%D1%80%D1%81%D1%82%D1%8F%D0%BD%D0%BE%D0%B9+%D0%BF%D0%B8%D0%B4%D0%BE%D1%80%D0%B0%D1%81"); //шерстяной пидорас
        urlThursdayList.add("https://old.reactor.cc/tag/%D0%B0%D0%B7%D0%B8%D0%B0%D1%82%D1%81%D0%BA%D0%B8%D0%B5+%D0%B2%D1%8B%D0%B4%D1%83%D0%BC%D1%89%D0%B8%D0%BA%D0%B8"); //азиатские выдумщики

        List<String> urlFridayList = new ArrayList<>();
        urlFridayList.add("https://reactor.cc/tag/Taurine+8000mg");    //Taurine 8000mg

        List<String> urlSaturdayList = new ArrayList<>();
        urlSaturdayList.add("https://reactor.cc/search/%D0%BA%D0%BE%D1%82%D1%8D");  //котэ
        urlSaturdayList.add("https://reactor.cc/search/%D1%81%D0%BE%D0%B1%D0%B0%D0%BA%D0%B0"); //собака
        urlSaturdayList.add("https://old.reactor.cc/tag/%D0%91%D1%80%D0%B0%D0%BA%D0%BE%D0%B2%D0%B0%D0%BD%D0%BD%D1%8B%D0%B9+%D0%BA%D0%BE%D1%82%D0%B8%D0%BA");  //Бракованный котик

        List<String> urlSundayList = new ArrayList<>();
        urlSundayList.add("https://old.reactor.cc/tag/%D0%BA%D0%BE%D1%82%D1%8D");  //котэ
        urlSundayList.add("https://old.reactor.cc/tag/%D1%81%D0%BE%D0%B1%D0%B0%D0%BA%D0%B0"); //собака
        urlSundayList.add("https://old.reactor.cc/tag/%D0%91%D1%80%D0%B0%D0%BA%D0%BE%D0%B2%D0%B0%D0%BD%D0%BD%D1%8B%D0%B9+%D0%BA%D0%BE%D1%82%D0%B8%D0%BA");  //Бракованный котик


        linkMap = new HashMap<>();
        linkMap.put(Calendar.MONDAY, urlMondayList);
        linkMap.put(Calendar.TUESDAY, urlTuesdayList);
        linkMap.put(Calendar.WEDNESDAY, urlWednesdayList);
        linkMap.put(Calendar.THURSDAY, urlThursdayList);
        linkMap.put(Calendar.FRIDAY, urlFridayList);
        linkMap.put(Calendar.SATURDAY, urlSaturdayList);
        linkMap.put(Calendar.SUNDAY, urlSundayList);

        captionMap = new HashMap<>();
        captionMap.put(Calendar.WEDNESDAY, "#ItIsWednesdayMyDudes");
        //captionMap.put(Calendar.FRIDAY, "#Friday");
    }



}
