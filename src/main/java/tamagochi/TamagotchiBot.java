package tamagochi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tamagochi.model.Reaction;
import tamagochi.pets.Beaver;
import tamagochi.pets.Tamagotchi;

@Component
public class TamagotchiBot extends TelegramLongPollingBot {

  private final Map<Long, Tamagotchi> tamagotchis = new HashMap<>();

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String messageText = update.getMessage().getText();
      long chatId = update.getMessage().getChatId();

      switch (messageText) {
        case "/start":
          start(chatId);
          break;
        case "/feed":
          feed(chatId);
          break;
        case "/play":
          play(chatId);
          break;
        case "/punish":
          punish(chatId);
          break;
        default:
          sendMessage(chatId, "Неизвестная команда. Используйте /start для начала.");
      }
    }
  }

  private void start(long chatId) {
    tamagotchis.put(chatId, new Beaver());
    sendMessage(chatId, "Ваш тамагочи создан! Ухаживайте за ним.\nИспользуйте команды /feed, /play и /punish.", createKeyboard());
  }

  private void feed(long chatId) {
    Tamagotchi tamagotchi = tamagotchis.get(chatId);
    if (tamagotchi != null) {
      Reaction reaction = tamagotchi.feed();
      sendPhoto(chatId, reaction.path(), reaction.message());
    } else {
      sendMessage(chatId, "Сначала создайте тамагочи с помощью /start.");
    }
  }

  private void play(long chatId) {
    Tamagotchi tamagotchi = tamagotchis.get(chatId);
    if (tamagotchi != null) {
      Reaction reaction = tamagotchi.play();
      sendPhoto(chatId, reaction.path(), reaction.message());
    } else {
      sendMessage(chatId, "Сначала создайте тамагочи с помощью /start.");
    }
  }

  private void punish(long chatId) {
    Tamagotchi tamagotchi = tamagotchis.get(chatId);
    if (tamagotchi != null) {
      Reaction reaction = tamagotchi.punish();
      sendPhoto(chatId, reaction.path(), reaction.message());
      if (!tamagotchi.isAlive()) {
        tamagotchis.remove(chatId);
        sendMessage(chatId, "Может следующему тамогочи повезет больше", createAfterDeathKeyboard());
      }
    } else {
      sendMessage(chatId, "Сначала создайте тамагочи с помощью /start.");
    }
  }

  private void sendMessage(long chatId, String text) {
    sendMessage(chatId, text, null);
  }

  private void sendMessage(long chatId, String text, ReplyKeyboardMarkup keyboard) {
    SendMessage message = new SendMessage();
    message.setChatId(String.valueOf(chatId));
    message.setText(text);
    message.setReplyMarkup(keyboard);
    try {
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }


  private void sendPhoto(long chatId, String path, String message) {
    SendPhoto photoMessage = new SendPhoto();
    photoMessage.setChatId(String.valueOf(chatId));
    photoMessage.setPhoto(new InputFile(new File(path)));
    photoMessage.setCaption(message);
    try {
      execute(photoMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private ReplyKeyboardMarkup createKeyboard() {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    keyboardMarkup.setSelective(true);
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setOneTimeKeyboard(false);


    KeyboardRow row1 = new KeyboardRow();
    row1.add("/feed");
    row1.add("/play");
    row1.add("/punish");
    List<KeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(row1);
    keyboardMarkup.setKeyboard(keyboard);

    return keyboardMarkup;
  }

  private ReplyKeyboardMarkup createAfterDeathKeyboard() {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    keyboardMarkup.setSelective(true);
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setOneTimeKeyboard(true);

    KeyboardRow row1 = new KeyboardRow();
    row1.add("/start");
    List<KeyboardRow> keyboard = new ArrayList<>();
    keyboard.add(row1);
    keyboardMarkup.setKeyboard(keyboard);

    return keyboardMarkup;
  }

  @Override
  public String getBotUsername() {
    return "TamagotchiTelegramBot";
  }

  @Override
  public String getBotToken() {
    return "7532318568:AAGiAeM1_2zs40x86WvGppJuxC-wFCUEgRY";
  }

  public void register() {
    try {
      TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
      botsApi.registerBot(this);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
  }

  @Value("bot.name")
  private String botUsername;

  @Value("bot.token")
  private String botToken;
}
