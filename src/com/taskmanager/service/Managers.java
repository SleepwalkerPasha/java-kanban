package com.taskmanager.service;

import com.taskmanager.client.HttpTaskManager;
import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.server.KVServer;

import java.io.IOException;
import java.net.URI;

public class Managers {

    public static HttpTaskManager getDefault(URI url, String apiToken) throws IOException {
        return new HttpTaskManager(url, apiToken);
    }

    public static ITaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static ITaskManager getFileBackedTaskManager(String filename) { return new FileBackedTasksManager(filename);}

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

}
