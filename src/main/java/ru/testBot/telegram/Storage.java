package ru.testBot.telegram;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Хранилище цитат
 */
public class Storage {
    private ArrayList<String> quoteList;
    Storage(String url)
    {
        quoteList = new ArrayList<>();
        parser(url);
    }

    String getRandQuote()
    {

        int randValue = (int)(Math.random() * quoteList.size());
        return quoteList.get(randValue);
    }

    /**
     * Парсер страницы
     * @param strURL
     */
    void parser(String strURL)
    {
        String className = "su-note-inner su-u-clearfix su-u-trim";
        Document doc = null;
        try {
            doc = Jsoup.connect(strURL).maxBodySize(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elQuote = doc.getElementsByClass(className);
        elQuote.forEach(el -> {
            quoteList.add(el.text());
        });
    }
}
