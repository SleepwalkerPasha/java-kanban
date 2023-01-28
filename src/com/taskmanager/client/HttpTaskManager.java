package com.taskmanager.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taskmanager.adapter.LocalDateTimeAdapter;
import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.FileBackedTasksManager;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient KVTaskClient;

    private final Gson gson;

    public HttpTaskManager(URI url, String apiToken) throws IOException {
        super();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        KVTaskClient = new KVTaskClient(url, apiToken);
    }

    public String getByKey(String key) throws IOException, InterruptedException {
        return KVTaskClient.load(key);
    }

    @Override
    public void save() {
        String json;
        for (Task task : regularTasks.values()) {
            json = gson.toJson(task);
            try {
                KVTaskClient.put(task.getId().toString(), json);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (Epic epic : epicTasks.values()) {
            json = gson.toJson(epic);
            try {
                KVTaskClient.put(epic.getId().toString(), json);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (SubTask subTask : subTasks.values()) {
            json = gson.toJson(subTask);
            try {
                KVTaskClient.put(subTask.getId().toString(), json);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String history = historyToString();
        try {
            KVTaskClient.put("history", history);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
