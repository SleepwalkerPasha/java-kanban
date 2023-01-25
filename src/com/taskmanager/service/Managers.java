package com.taskmanager.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taskmanager.client.HttpTaskManager;
import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

public class Managers {

    public static ITaskManager getDefault(URI url, KVServer server) throws IOException {
        return new HttpTaskManager(url, server);
    }

    public static ITaskManager getFileBackedTaskManager(String filename) { return new FileBackedTasksManager(filename);}

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

}
