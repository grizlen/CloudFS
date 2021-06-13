package client.preferences;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
public class Preferences {

    private static Preferences preferences;
    private final HashMap<String, String> map;

    public static Preferences getPreferences() {
        if (preferences == null) {
            preferences = new Preferences();
        }
        return preferences;
    }

    public Preferences() {
        map = new HashMap<>();
        load();
    }

    public static void close() {
        if (preferences != null) {
            preferences.save();
            preferences = null;
        }
    }

    private void save() {
        Path path = Paths.get("CloudClient", "pref", "pref.txt");
        try {
            if (Files.notExists(path.getParent())){
                Files.createDirectory(path.getParent());
            }
            Files.write(
                    path,
                    map.entrySet().stream()
                            .map(e -> e.getKey() + "=" + e.getValue())
                            .collect(Collectors.toList())
                    );
        } catch (IOException e) {
            log.error("save preference exception", e);
        }
    }

    private void load() {
        Path path = Paths.get("CloudClient", "pref", "pref.txt");
        map.clear();
        if (Files.exists(path)) {
            try {
                Files.lines(path).forEach(l -> map.put(l.split("=")[0], l.split("=")[1]));
            } catch (IOException e) {
                log.error("Load pref exception:",e);
            }
        } else {
            map.put("userName", "user");
            map.put("userPassword", "password");
            map.put("serverHost", "localhost");
            map.put("serverPort", "8189");
            map.put("localPathName", "Client/client-files");
        }
    }

    public String get(String key) {
        String result = map.get(key);
        return result == null ? "" : result;
    }

    public void put(String key, String value) {
        map.put(key, value);
    }
}
