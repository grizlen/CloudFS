package cloudclient.preferences;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
public class Preferences {

    private static Preferences preferences;

    public static Preferences getPreferences(){
        if (preferences == null) {
            preferences = new Preferences();
        }
        return preferences;
    }

    public static void close(){
        if (preferences != null){
            preferences.save();
            preferences = null;
        }
    }

    private final HashMap<String , String> data;

    private Preferences() {
        data = new HashMap<>();
        load();
//        data.put("userName", "user");
//        data.put("userPassword", "password");
//        data.put("serverHost", "localhost");
//        data.put("serverPort", "8189");
//        data.put("localPathName", "Client/client-files");
    }

    public String get(String key){
        return data.get(key);
    }

    public void put(String key, String value){
        data.put(key, value);
    }

    private void load() {
        try {
            Files.lines(Paths.get("Client", "pref", "preference.txt"))
                    .forEach(l -> data.put(l.split("=")[0], l.split("=")[1]));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void save(){
        try {
            Files.write(
                    Paths.get("Client", "pref", "preference.txt"),
                    data.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.toList()),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.debug("Preferences saved.");
    }
}
