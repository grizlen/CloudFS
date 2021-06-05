package service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Pref {

    private static Pref pref;

    public static Pref getPref(){
        if (pref == null) {
            pref = new Pref();
        }
        return pref;
    }

    public static void close() {
        if (pref != null) {
            pref.save();
        }
    }

    private Map<String, String> values;

    public Pref() {
        values = new HashMap<>();
        values.put("userName", "user");
        values.put("userPassword", "password");
    }

    public String getValue(String key){
        return values.get(key);
    }

    private void save(){
        values.forEach((k, v) -> System.out.println("key "+ k + " = " + v));
        System.out.println("Preference saved.");
    }
}
