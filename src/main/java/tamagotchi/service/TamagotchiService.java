package tamagotchi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tamagotchi.exception.TamagothiNotFoundException;
import tamagotchi.model.Reaction;
import tamagotchi.model.SendMessageDto;
import tamagotchi.model.SendPhotoDto;
import tamagotchi.pet.Beaver;
import tamagotchi.pet.Tamagotchi;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TamagotchiService {

    private final String START_MESSAGE = "tamagotchi.created";
    private final String CHOOSE_PET_MESSAGE = "tamagotchi.choose";

    private final Map<Long, Tamagotchi> tamagotchis = new ConcurrentHashMap<>();
    private final TamagotchiFactory tamagotchiFactory;


    public List<String> getAvailableTypes() {
        return tamagotchiFactory.getAvailableTypes();
    }

    public SendMessageDto chooseTamagotchi(long chatId) {
        return new SendMessageDto(chatId, CHOOSE_PET_MESSAGE, KeyboardType.CREATE_OPTIONS);
    }

    public SendMessageDto createTamagotchi(long chatId, String type) {
        Tamagotchi tama = tamagotchiFactory.create(type);
        tamagotchis.put(chatId, tama);
        return new SendMessageDto(chatId, START_MESSAGE, KeyboardType.OPTIONS);
    }

    public SendPhotoDto feed(long chatId) {
        Reaction reaction = getTamagotchi(chatId).feed();
        return new SendPhotoDto(chatId, reaction.message(), KeyboardType.NON, reaction.path());
    }

    public SendPhotoDto play(long chatId) {
        Reaction reaction = getTamagotchi(chatId).play();
        return new SendPhotoDto(chatId, reaction.message(), KeyboardType.NON, reaction.path());
    }

    public SendPhotoDto punish(long chatId) {
        Tamagotchi tamagotchi = getTamagotchi(chatId);
        Reaction reaction = tamagotchi.punish();
        if (!tamagotchi.isAlive()) {
            tamagotchis.remove(chatId);
            return new SendPhotoDto(chatId, reaction.message(), KeyboardType.START, reaction.path());
        }
        return new SendPhotoDto(chatId, reaction.message(), KeyboardType.NON, reaction.path());
    }

    public List<SendPhotoDto> abandon() {
        return tamagotchis.entrySet().stream().map(entry -> {
            Reaction reaction = entry.getValue().abandon();
            if (reaction != null) {
                tamagotchis.remove(entry.getKey());
                return new SendPhotoDto(entry.getKey(), reaction.message(), KeyboardType.START, reaction.path());
            }
            return null;
        }).filter(Objects::nonNull).toList();
    }

    private Tamagotchi getTamagotchi(long chatId) {
        return Optional.ofNullable(tamagotchis.get(chatId)).orElseThrow(TamagothiNotFoundException::new);
    }

    @Autowired
    public TamagotchiService(TamagotchiFactory tamagotchiFactory) {
        this.tamagotchiFactory = tamagotchiFactory;
    }
}
