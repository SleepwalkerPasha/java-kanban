package com.taskmanager.service;

import com.taskmanager.interfaces.IHistoryManager;
import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements IHistoryManager {

    private List<Task> taskList = new ArrayList<>(10);

    public int getSize(){
        return taskList.size();
    }

    @Override
    public void add(Task task) {
        if (taskList.size() >= 10)
            taskList.remove(0);
        taskList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return taskList;
    }
}
