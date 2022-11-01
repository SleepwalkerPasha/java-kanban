package com.taskmanager.model;

public class SubTask extends Task {
    private Integer masterId;

    public SubTask(String name, String description, Integer newMasterId) {
        super(name, description);
        masterId = newMasterId;
    }

    public SubTask(String name, String description, Status status, Integer id, Integer newMasterId) {
        super(name, description, status, id);
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
        return "SubTask{" +
                "master='" + masterId + ", " +
                super.toString() +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return super.equals(subTask) && this.masterId.equals(subTask.masterId);
    }

}
