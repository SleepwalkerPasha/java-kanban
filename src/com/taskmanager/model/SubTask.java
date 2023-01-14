package com.taskmanager.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private Integer masterId;

    public SubTask(String name, String description, Duration duration, LocalDateTime startTime, Integer newMasterId) {
        super(name, description, duration, startTime);
        masterId = newMasterId;
    }

    public SubTask(String name, String description, Status status, Integer id, Duration duration, LocalDateTime startTime, Integer newMasterId) {
        super(name, description, status, id, duration, startTime);
        masterId = newMasterId;
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    @Override
    public String toString() {
        return super.toString() + masterId + "|";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return super.equals(subTask) && Objects.equals(masterId, subTask.masterId);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + masterId.hashCode();
    }
}
