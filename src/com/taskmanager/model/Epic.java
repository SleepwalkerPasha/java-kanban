package com.taskmanager.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksIds() {
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
        return "Epic{" +
                super.toString() + ", " +
                "subTasksIds=" + subTasksIds.toString() +
                '}';
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
