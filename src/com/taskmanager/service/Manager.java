package com.taskmanager.service;

import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int countOfTasks;

    private HashMap<Integer, Task> regularTasks;

    private HashMap<Integer, Epic> epicTasks;

    private HashMap<Integer, SubTask> subTasks;

    public Manager(int countOfTasks, HashMap<Integer, Task> regularTasks, HashMap<Integer, Epic> epicTasks, HashMap<Integer, SubTask> subTasks) {
        this.countOfTasks = countOfTasks;
        this.regularTasks = regularTasks;
        this.epicTasks = epicTasks;
        this.subTasks = subTasks;
    }

    public Task getTaskById(Integer id){
        if (regularTasks.containsKey(id))
            return regularTasks.get(id);
        else if (epicTasks.containsKey(id))
            return epicTasks.get(id);
        else if (subTasks.containsKey(id))
            return subTasks.get(id);
        return null;
    }

    public HashMap<Integer, Task> getRegularTasks(){
        return regularTasks;
    }

    public HashMap<Integer, Epic> getEpicTasks(){
        return epicTasks;
    }

    public HashMap<Integer, SubTask> getSubTasks(){
        return subTasks;
    }

    public int getCountOfTasks() {
        return countOfTasks;
    }

    public void updateTask(Task task){
        if (task.getClass().equals(Task.class)){
            if (regularTasks.containsKey(task.getId()))
                regularTasks.put(task.getId(), task);
        }else if (task.getClass().equals(Epic.class)){
            Epic epic = (Epic) task;
            if (epicTasks.containsKey(epic.getId()))
                epicTasks.put(epic.getId(), epic);
        }else if (task.getClass().equals(SubTask.class)){
            SubTask sTask = (SubTask) task;
            if (subTasks.containsKey(sTask.getId()))
                subTasks.put(sTask.getId(), sTask);
        }
    }

    public void makeTask(Task task){
        if (task.getClass().equals(Task.class)){
            regularTasks.put(task.getId(), task);
        }else if (task.getClass().equals(Epic.class)){
            Epic epic = (Epic) task;
            epicTasks.put(epic.getId(), epic);
        }else if (task.getClass().equals(SubTask.class)){
            SubTask sTask = (SubTask) task;
            subTasks.put(sTask.getId(), sTask);
        }
        countOfTasks++;
    }

    public void removeAllTasks(){
        regularTasks.clear();
    }

    public void removeAllEpicTasks(){
        epicTasks.clear();
        removeAllSubTasks();
    }

    public void removeAllSubTasks(){
        subTasks.clear();
    }

    public void removeById(Integer id){
        if (regularTasks.containsKey(id))
            regularTasks.remove(id);
        else if (epicTasks.containsKey(id))
            epicTasks.remove(id);
        else if (subTasks.containsKey(id))
            subTasks.remove(id);
    }

    public ArrayList<SubTask> subTasksOfEpicTask(Integer id){
        if (epicTasks.containsKey(id))
            return epicTasks.get(id).getSubTasks();
        else
            return null;
    }
}
