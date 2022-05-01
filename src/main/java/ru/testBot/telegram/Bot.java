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


    final private String BOT_TOKEN = "5344931381:AAGftdp_Q9U8skmmcE227ziTho_8xjbfML0";
    final private String BOT_NAME = "example27042022_bot";
    private Storage storage;
    private ReplyKeyboardMarkup replyKeyboardMarkup;

    Bot() {
        storage = new Storage();
        initKeyboard();
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    /**
     * Метод для обработки и отправки сообщения в чат.
     * @param update - объект который приходит от пользователя.
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
            response = "Приветствую, бот знает много цитат. Жми /get, чтобы получить случайную из них";
        else if(textMsg.equals("/get") || textMsg.equals("Просвяти"))
            response = storage.getRandQuote();
        else
            response = "Сообщение не распознано";

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
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }


}
