package com.taskmanager.model;


public class Task {
    private String name;
    private String description;
    private Integer id;
    private Status status;

    public Task(String name, String description, Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Integer id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
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

    protected void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + "', " +
                "description='" + description + "', " +
                "id=" + id + ", " +
                "status=" + status +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name) && description.equals(task.description);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null)
            hash *= name.hashCode();
        if (description != null)
            hash += 31 * description.hashCode();
        if (status != null)
            hash += 11 * status.hashCode();
        return hash;
    }

}
