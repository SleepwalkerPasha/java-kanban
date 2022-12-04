package com.taskmanager.interfaces;

import com.taskmanager.model.Task;

import java.util.List;

public interface IHistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
