package com.home.mytelegrambot.service;

import com.home.mytelegrambot.config.JoyConfig;
import com.home.mytelegrambot.dao.MyRepository;
import com.home.mytelegrambot.dto.MemeContent;
import com.home.mytelegrambot.dto.MemeSql;
import com.home.mytelegrambot.dto.TypeContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
public class MemeService {
    private JoyConfig joyConfig;
    private MyRepository myRepository;

    private String getDayCaption() {
        return joyConfig.getCaptionMap().getOrDefault(Calendar.getInstance().get(Calendar.DAY_OF_WEEK),"");
    }

    public MemeContent getDayMeme() {
        try {
            List<String> dayUrlList = joyConfig.getLinkMap().get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
            String url = dayUrlList.get((int) (Math.random() * dayUrlList.size()));

            Document document = Jsoup.connect(url).userAgent(joyConfig.getUserAgent()).referrer(joyConfig.getReferrer()).get();
            Elements pagination = document.getElementsByClass("pagination");
            Elements pageNumbers = pagination.getFirst().getElementsByClass("current");
            int page = Integer.parseInt(pageNumbers.get(1).text());

            while (page > 0) {
                String link = getVideoMeme(url + "/" + page);
                if (!link.isEmpty()) return new MemeContent(link, TypeContent.VIDEO, getDayCaption());

                link = getImageMeme(url + "/" + page);
                if (!link.isEmpty()) return new MemeContent(link, TypeContent.IMAGE, getDayCaption());

                log.debug("On page {} meme not found", page);
                page--;
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    private String getImageMeme(String url) {
        try {
            Document document = Jsoup.connect(url).userAgent(joyConfig.getUserAgent()).referrer(joyConfig.getReferrer()).get();
            Elements images = document.getElementsByClass("prettyPhotoLink");
            log.debug("Meme found: " + images.size());

            if (images.isEmpty()) {
                return "";

            } else {
                for (Element image : images) {
                    String link = image.attr("href").replace("reactor.cc/pics/post/full/", "reactor.cc/pics/post/");
                    log.debug(link);

                    if (!myRepository.isPresentByLink(link)) {
                        log.debug("Good link found");
                        myRepository.saveMemeLink(new MemeSql(0, new Timestamp(System.currentTimeMillis()), link));
                        return "https:" + link;
                    } else {
                        log.debug("Used link found");
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return "";
    }

    private String getVideoMeme(String url) {
        try {
            Document document = Jsoup.connect(url).userAgent(joyConfig.getUserAgent()).referrer(joyConfig.getReferrer()).get();
            Elements videos = document.getElementsByAttributeValue("type", "video/mp4");
            log.debug("Meme found: " + videos.size());

            if (videos.isEmpty()) {
                return "";

            } else {
                for (Element video : videos) {
                    String link = video.attr("src");
                    log.debug(link);

                    if (!myRepository.isPresentByLink(link)) {
                        log.debug("Good meme found");
                        myRepository.saveMemeLink(new MemeSql(0, new Timestamp(System.currentTimeMillis()), link));
                        return "https:" + link;
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return "";
    }

}
