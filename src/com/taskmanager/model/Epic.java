package com.taskmanager.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTasksIds;

    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subTasksIds = new ArrayList<>();
    }
    public Epic(String name, String description, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        subTasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, Status status, Integer id, Duration duration, LocalDateTime startTime, List<Integer> subtasks) {
        super(name, description, status, id, duration, startTime);
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

    @Override
    public int hashCode() {
        return super.hashCode() + subTasksIds.hashCode();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
