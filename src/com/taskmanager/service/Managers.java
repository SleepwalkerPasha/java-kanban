package com.taskmanager.service;

import com.taskmanager.interfaces.ITaskManager;

public class Managers {

    public static ITaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static ITaskManager getFileBackedTaskManager(String filename) { return new FileBackedTasksManager(filename);}

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
