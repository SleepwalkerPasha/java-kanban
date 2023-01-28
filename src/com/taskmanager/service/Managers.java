package com.taskmanager.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taskmanager.adapter.LocalDateTimeAdapter;
import com.taskmanager.client.HttpTaskManager;
import com.taskmanager.interfaces.ITaskManager;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

public class Managers {

    public static HttpTaskManager getDefault(URI url, String apiToken) throws IOException {
        return new HttpTaskManager(url, apiToken);
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
    }

    public static ITaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static ITaskManager getFileBackedTaskManager(String filename) { return new FileBackedTasksManager(filename);}

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

}
