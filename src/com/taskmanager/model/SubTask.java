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
                "master='" + master.getName() + ", " +
                super.toString() +
                "}";
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return super.equals(subTask) && this.master.equals(subTask.master);
    }

    public void setStatus(Status status){
        super.setStatus(status);
    }

}
