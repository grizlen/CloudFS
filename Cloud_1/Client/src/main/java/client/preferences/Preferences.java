package client.preferences;

import java.util.HashMap;

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
        data.put("userName", "user");
        data.put("userPassword", "password");
        data.put("serverHost", "localhost");
        data.put("serverPort", "8189");
        data.put("localPathName", "Client/client-files");
    }

    public String get(String key){
        return data.get(key);
    }

    public void put(String key, String value){
        data.put(key, value);
    }

    private void save(){
        System.out.println("Preferences saved.");
    }
}
