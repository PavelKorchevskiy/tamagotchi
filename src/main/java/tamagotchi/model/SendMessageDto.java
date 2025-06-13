package tamagotchi.model;

import tamagotchi.service.KeyboardType;

public record SendMessageDto(long chatId, String message, KeyboardType keyboardType) {
}
