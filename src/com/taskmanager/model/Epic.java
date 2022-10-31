package com.taskmanager.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(String name, String description, Integer id) {
        super(name, description, id);
        subTasks = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask) {
        if (subTasks.contains(subTask)) {
            int index = subTasks.indexOf(subTask);
            this.subTasks.set(index, subTask);
        } else
            this.subTasks.add(subTask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                super.toString() + ", \n" +
                "subTasks=" + subTasks.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return super.equals(epic) && this.subTasks.equals(epic.subTasks);
    }

    public void checkSubtasksStatus() {
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
