package tamagotchi.model;

import tamagotchi.service.KeyboardType;

public record SendPhotoDto(long chatId, String message, KeyboardType keyboardType, String photoPath) {
}
