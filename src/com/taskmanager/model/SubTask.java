package com.taskmanager.model;

public class SubTask extends Task{
    private Integer masterId;

    public SubTask(String name, String description, Integer id, Integer newMasterId) {
        super(name, description, id);
        masterId = newMasterId;
    }


    public Integer getMaster() {
        return masterId;
    }

    public void setMaster(Integer masterId) {
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
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return super.equals(subTask) && this.masterId.equals(subTask.masterId);
    }

    public void setStatus(Status status){
        super.setStatus(status);
    }

}
