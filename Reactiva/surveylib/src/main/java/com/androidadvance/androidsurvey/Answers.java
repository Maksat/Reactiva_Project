package com.androidadvance.androidsurvey;

import com.androidadvance.androidsurvey.models.Answer;
import com.google.gson.Gson;


import java.util.Date;
import java.util.LinkedHashMap;

//Singleton Answers ........

public class Answers {

    private volatile static Answers uniqueInstance;
    private final LinkedHashMap<String, Answer> answered_hashmap = new LinkedHashMap<>();

    private Date sessionStart;
    private Date sessionEnd;

    private Answers() {
        sessionStart = new Date();
    }

    public void put_answer(String key, Answer value) {
        answered_hashmap.put(key, value);
    }

    public String get_json_object() {
        sessionEnd = new Date();

        LinkedHashMap<String, Object> session_hashmap = new LinkedHashMap<>();

        session_hashmap.put("session_start", sessionStart);
        session_hashmap.put("session_end", sessionEnd);
        session_hashmap.put("user_id", "1");
        session_hashmap.put("answers", answered_hashmap);

        Gson gson = new Gson();
        return gson.toJson(session_hashmap,LinkedHashMap.class);
    }

    @Override
    public String toString() {
        return String.valueOf(answered_hashmap);
    }

    public static Answers getInstance() {
        if (uniqueInstance == null) {
            synchronized (Answers.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Answers();
                }
            }
        }
        return uniqueInstance;
    }
}
