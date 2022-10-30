package com.taskmanager.model;

public class SubTask extends Task{
    private Epic master;

    public SubTask(String name, String description, Integer id, Epic newMaster) {
        super(name, description, id);
        master = newMaster;
    }

    public Epic getMaster() {
        return master;
    }

    public void setMaster(Epic master) {
        this.master = master;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "master=" + master +
                super.toString() +
                '}';
    }
}
