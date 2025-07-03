package tamagotchi.service;

import org.springframework.stereotype.Service;
import tamagotchi.pet.Beaver;
import tamagotchi.pet.PixelBeaver;
import tamagotchi.pet.Tamagotchi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class TamagotchiFactory {

    private final Map<String, Supplier<Tamagotchi>> factories = new HashMap<>();

    public TamagotchiFactory() {
        factories.put("Beaver", Beaver::new);
        factories.put("Pixel Beaver", PixelBeaver::new);
    }

    public List<String> getAvailableTypes() {
        return new ArrayList<>(factories.keySet());
    }

    public Tamagotchi create(String type) {
        Supplier<Tamagotchi> factory = factories.get(type);
        if (factory == null) throw new IllegalArgumentException("Unknown tamagotchi type: " + type);
        return factory.get();
    }
}
