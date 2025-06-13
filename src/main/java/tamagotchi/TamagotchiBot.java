package tamagotchi;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tamagotchi.model.SendMessageDto;
import tamagotchi.model.SendPhotoDto;
import tamagotchi.service.KeyboardType;
import tamagotchi.service.TamagotchiService;

@Component
public class TamagotchiBot extends TelegramLongPollingBot {

  private TamagotchiService service;

  private final static String UNKNOWN_COMMAND = "Неизвестная команда. Используйте /start для начала.";

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String messageText = update.getMessage().getText();
      long chatId = update.getMessage().getChatId();

      switch (messageText) {
        case "/start":
          sendMessage(service.createNewTamagotchi(chatId));
          break;
        case "/feed":
          sendPhoto(service.feed(chatId));
          break;
        case "/play":
          sendPhoto(service.play(chatId));
          break;
        case "/punish":
          sendPhoto(service.punish(chatId));
          break;
        default:
          sendMessage(new SendMessageDto(chatId, UNKNOWN_COMMAND, KeyboardType.START));
      }
    }
  }

  public void abandon() {
    List<SendPhotoDto> deadTamagotchiResponse = service.abandon();
    deadTamagotchiResponse.forEach(this::sendPhoto);
  }

  private void sendMessage(SendMessageDto sendMessageDto) {
    SendMessage message = new SendMessage();
    message.setChatId(String.valueOf(sendMessageDto.chatId()));
    message.setText(sendMessageDto.message());
    message.setReplyMarkup(createKeyboard(sendMessageDto.keyboardType()));
    try {
      execute(message);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private void sendPhoto(SendPhotoDto sendPhotoDto) {
    SendPhoto photoMessage = new SendPhoto();
    photoMessage.setChatId(String.valueOf(sendPhotoDto.chatId()));
    photoMessage.setPhoto(createPhotoFromResource(sendPhotoDto.photoPath()));
    photoMessage.setCaption(sendPhotoDto.message());
    photoMessage.setReplyMarkup(createKeyboard(sendPhotoDto.keyboardType()));

    try {
      execute(photoMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  private InputFile createPhotoFromResource(String resourcePathWithFilename) {
    // Извлекаем имя файла из пути
    String filename = Paths.get(resourcePathWithFilename).getFileName().toString();

    // Загружаем как InputStream
    InputStream is = getClass().getResourceAsStream(resourcePathWithFilename);
    if (is == null) {
      throw new RuntimeException("Ресурс не найден: " + resourcePathWithFilename);
    }

    // Создаём InputFile
    InputFile inputFile = new InputFile();
    inputFile.setMedia(is, filename);

    return inputFile;
  }

  private ReplyKeyboardMarkup createKeyboard(KeyboardType type) {
    return switch (type) {
      case OPTIONS -> {
        yield createOptionsKeyboard();
      }
      case START -> {
        yield createStartKeyboard();
      }
      default -> {
        yield null;
      }
    };
  }

  private ReplyKeyboardMarkup createOptionsKeyboard() {
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

  private ReplyKeyboardMarkup createStartKeyboard() {
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
    return botToken;
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

  @Value("${bot.token}")
  private String botToken;

  @Autowired
  public void setService(TamagotchiService service) {
    this.service = service;
  }
}
