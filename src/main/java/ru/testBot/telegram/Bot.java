package ru.testBot.telegram;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;


public class Bot extends TelegramLongPollingBot {


    private String tokenBot;
    private String nameBot;
    private Storage storage;
    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private WeatherCache weatherCache;

    Bot() {
        storage = new Storage("https://citatnica.ru/citaty/mudrye-tsitaty-velikih-lyudej");
        initKeyboard();
    }

    public void setTokenBot(String tokenBot) {
        this.tokenBot = tokenBot;
    }

    public void setNameBot(String nameBot) {
        this.nameBot = nameBot;
    }

    public void setWeatherCache(WeatherCache weatherCache) {
        this.weatherCache = weatherCache;
    }

    @Override
    public String getBotUsername() {
        return nameBot;
    }

    @Override
    public String getBotToken() {
        return tokenBot;
    }

    /**
     * Метод для обработки и отправки сообщения в чат.
     * @param update - объект, который приходит от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        try{
            if(update.hasMessage() && update.getMessage().hasText())
            {
                Message inMess = update.getMessage();
                String chatId = inMess.getChatId().toString();
                String response = parseMessage(inMess.getText());
                SendMessage outMess = new SendMessage();
                outMess.setReplyMarkup(replyKeyboardMarkup);
                outMess.setChatId(chatId);
                outMess.setText(response);
                execute(outMess);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сравниваем текст введенный пользователем с имеющимися командами
     * @param textMsg - сообщение пользователя
     * @return - ответ
     */
    public String parseMessage(String textMsg) {
        String response;


        if(textMsg.equals("/start"))
            response = "Приветствую, бот знает много цитат. Жми /get, чтобы получить случайную из них," +
                    "еще здесь можно узнать погоду в городе!";

        else if(textMsg.equals("/get") || textMsg.equals("Просвяти"))
            response = storage.getRandQuote();
        else if (textMsg.equals("Узнать погоду!")){
            response = "Введите название города!";
        }
        else {
            WeatherInfo weatherInfo = weatherCache.getWeatherInfo(textMsg);
            if (weatherInfo != null) {
                response = weatherInfo.toString();
            }
            else {
                response = "Сообщение не распознано!";
            }

        }

        return response;
    }

    /**
     * Добавляем клавиатуру.
     */
    public void initKeyboard()
    {

        replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRow.add(new KeyboardButton("Просвяти"));
        keyboardRow.add(new KeyboardButton("Узнать погоду!"));
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
}
