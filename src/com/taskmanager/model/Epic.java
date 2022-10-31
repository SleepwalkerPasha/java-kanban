package com.taskmanager.model;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, Integer id) {
        super(name, description, id);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        this.subTasks.add(subTask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                super.toString() + ", \n"+
                "subTasks=" + subTasks.toString() +
                '}';
    }

    public void checkSubtasksStatus(){
        int countNew = 0;
        int countDone = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus() == Status.NEW)
                countNew++;
            else if (subTask.getStatus() == Status.DONE)
                countDone++;
        }
        if (countNew == subTasks.size())
            setStatus(Status.NEW); // epic task must be NEW cause all subtasks is new
        else if (countDone == subTasks.size())
            setStatus(Status.DONE);// epic task must be DONE cause all subtasks in DONE
        else
            setStatus(Status.IN_PROGRESS);
    }
}
