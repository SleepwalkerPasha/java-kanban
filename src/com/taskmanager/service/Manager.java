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

    public Manager() {
        this.regularTasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        countOfTasks = 0;
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

    public HashMap<Integer, SubTask> getSubtasks(){
        return subTasks;
    }

    public int getCountOfTasks() {
        return countOfTasks;
    }

    public void updateTask(Task task) {

    }

    public void createTask(Task task){

    }

    public void removeAllTasks(){
        regularTasks.clear();
    }

    public void removeAllEpicTasks(){
        epicTasks.clear();
        removeAllSubtasks();
    }

    public void removeAllSubtasks(){
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

    public ArrayList<Integer> subTasksIdsOfEpicTask(Integer id){
        ArrayList<Integer> ids = epicTasks.get(id).getSubTasksIds();
        return ids;
    }
}
