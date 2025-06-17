package tamagotchi.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MessageService {

    private final Map<Locale, Properties> messages = new HashMap<>();

    @PostConstruct
    public void loadMessages() {
        try {
            String baseDir = "src/main/resources/locales/";
            List<Path> files = Files.list(Paths.get(baseDir))
                    .filter(path -> path.toString().endsWith(".properties"))
                    .toList();

            for (Path file : files) {
                String filename = file.getFileName().toString();
                String langCode = filename.replace("messages_", "").replace(".properties", "");

                Properties props = new Properties();
                props.load(Files.newInputStream(file));

                Locale locale = new Locale(langCode);
                messages.put(locale, props);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки локализационных файлов", e);
        }
    }

    public String get(String key, Locale locale) {
        Properties props = messages.getOrDefault(locale, messages.get(Locale.getDefault()));
        return props.getProperty(key, "???" + key + "???");
    }
}
