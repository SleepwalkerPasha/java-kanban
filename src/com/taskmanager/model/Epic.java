package com.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, Integer id, List<Integer> subtasks) {
        super(name, description, status, id);
        subTasksIds = subtasks;
    }

    public List<Integer> getSubTasksIds() {
        return subTasksIds;
    }

    public void addSubtaskId(Integer subTaskId) {
        int index = subTasksIds.indexOf(subTaskId);
        if (index != -1)
            this.subTasksIds.set(index, subTaskId);
        else
            this.subTasksIds.add(subTaskId);
    }

    @Override
    public String toString() {
        return super.toString() + "|" + subTasksIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        if (epic.subTasksIds == null)
            return false;
        return super.equals(epic) && this.subTasksIds.equals(epic.subTasksIds);
    }

}
