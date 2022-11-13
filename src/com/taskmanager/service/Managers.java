package com.taskmanager.service;

import com.taskmanager.interfaces.ITaskManager;

public class Managers {

    public static ITaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static InMemoryHistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}
