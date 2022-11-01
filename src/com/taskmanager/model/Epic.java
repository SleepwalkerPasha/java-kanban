package com.taskmanager.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasksIds;

    public Epic(String name, String description, Integer id) {
        super(name, description, id);
        subTasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubTasksIds() {
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
        return "Epic{" +
                super.toString() + ", \n" +
                "subTasksIds=" + subTasksIds.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return super.equals(epic) && this.subTasksIds.equals(epic.subTasksIds);
    }

//    public void checkSubtasksIdsStatus() {
//        int countNew = 0;
//        int countDone = 0;
//        for (Integer id : subTasksIds) {
//            if (subTask.getStatus() == Status.NEW)
//                countNew++;
//            else if (subTask.getStatus() == Status.DONE)
//                countDone++;
//        }
//        if (countNew == subTasksIds.size())
//            setStatus(Status.NEW); // epic task must be NEW cause all subTasksIds is new
//        else if (countDone == subTasksIds.size())
//            setStatus(Status.DONE);// epic task must be DONE cause all subTasksIds in DONE
//        else
//            setStatus(Status.IN_PROGRESS);
//    }
}
