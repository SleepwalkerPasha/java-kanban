package com.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private List<SubTask> subTasks;

    public Epic(String name, String description, Integer id, Status status) {
        super(name, description, id, status);
        subTasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                super.toString() +
                "subTasks=" + subTasks.toString() +
                '}';
    }
}
