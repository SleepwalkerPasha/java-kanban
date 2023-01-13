package com.taskmanager.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Integer id;
    private Status status;

    protected Duration duration;

    protected LocalDateTime startTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = null;
        this.status = Status.NEW;
        this.duration = null;
        this.startTime = null;
    }

    public Task(String name, String description, Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
        this.duration = null;
        this.startTime = null;
    }

    public Task(String name, String description, Status status, Integer id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Duration duration, LocalDateTime localDateTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = localDateTime;
        this.status = Status.NEW;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null)
            return startTime.plus(duration);
        return null;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    @Override
    public String toString() {
        return id + "|" + getClass().toString().substring(28) + "|" + name + "|" + status + "|" + description + "|";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;

        // недобавил проверку на null, поэтому и вылетает NullPointerException
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(id, task.id) && Objects.equals(status, task.status)
                && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null)
            hash += name.hashCode();
        hash *= 31;
        if (description != null)
            hash += description.hashCode();
        hash *= 11;
        if (status != null)
            hash += status.hashCode();
        hash *= 13;
        if (id != null)
            hash += id;
        if (duration != null)
            hash += duration.hashCode();
        hash *= 13;
        if (startTime != null)
            hash += startTime.hashCode();
        return hash;
    }

}
