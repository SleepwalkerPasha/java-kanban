package com.taskmanager.service;

import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.IntBinaryOperator;

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

    public Task getTaskById(Integer id) { // методы для каждого типа задач
        return regularTasks.get(id);
    }

    public SubTask getSubtaskById(Integer id){
        return subTasks.get(id);
    }

    public Epic getEpicById(Integer id){
        return epicTasks.get(id);
    }

    public ArrayList<Task> getRegularTasks() { // изменил получения значений, вношу в массив, чтобы не получать доступ к map
        return new ArrayList<>(regularTasks.values());
    }

    public ArrayList<Epic> getEpicTasks() { // изменил получения значений, вношу в массив, чтобы не получать доступ к map
        return new ArrayList<>(epicTasks.values());
    }

    public ArrayList<SubTask> getSubtasks() { // изменил получения значений, вношу в массив, чтобы не получать доступ к map
        return new ArrayList<>(subTasks.values());
    }

    public int getCountOfTasks() {
        return countOfTasks;
    }

    public void updateTask(Task task) {
        int id = task.getId();
        Task oldTask = regularTasks.get(id);
        if (oldTask == null)
            return;
        regularTasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic oldEpic = epicTasks.get(id);
        if (oldEpic == null)
            return;
        epicTasks.put(id, epic);
    }

    public void updateSubtask(SubTask subTask) {
        int id = subTask.getId();
        SubTask oldSubtask = subTasks.get(id);
        if (oldSubtask == null)
            return;
        Epic epic = epicTasks.get(oldSubtask.getMasterId());
        if (epic == null)
            return;
        subTasks.put(id, subTask);
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> tasks = epic.getSubTasksIds();
        if (tasks.isEmpty()){
            epic.setStatus(Status.NEW);
            return;
        }
        int countNew = 0;
        int countDone = 0;
        for (Integer id : tasks) {
            SubTask subTask = subTasks.get(id);
            if (subTask == null)
                return;
            if (subTask.getStatus() == Status.DONE)
                countDone++;
            else if (subTask.getStatus() == Status.NEW)
                countNew++;
        }
        if (countNew == tasks.size())
            epic.setStatus(Status.NEW);
        else if (countDone == tasks.size())
            epic.setStatus(Status.DONE);
        else
            epic.setStatus(Status.IN_PROGRESS);
    }

    public int createNewTask(Task task) {
        int id = ++countOfTasks;
        task.setId(id);
        regularTasks.put(id, task);
        return id;
    }

    public Integer createNewSubtask(SubTask subTask) {
        int id = ++countOfTasks;
        subTask.setId(id);
        Epic epic = epicTasks.get(subTask.getMasterId());
        if (epic == null)
            return null;
        epic.addSubtaskId(id);
        subTasks.put(id, subTask);
        updateEpicStatus(epic);
        return id;
    }

    public int createNewEpic(Epic epic) {
        int id = ++countOfTasks;
        epic.setId(id);
        epicTasks.put(id, epic);
        return id;
    }

    public void removeAllTasks() {
        if (!regularTasks.isEmpty())
            regularTasks.clear();
    }

    public void removeAllEpicTasks() {
        if (!epicTasks.isEmpty()){
            epicTasks.clear();
            removeAllSubtasks();
        }
    }

    public void removeAllSubtasks() {
        for (SubTask value : subTasks.values()) {
            Epic epic = getEpicById(value.getMasterId());
            if (epic == null)
                return;
            epic.getSubTasksIds().clear();
            updateEpicStatus(epic);
        }
        if (!subTasks.isEmpty())
            subTasks.clear();
    }

    public void removeTaskById(Integer id) {
        regularTasks.remove(id);
    }
    public void removeSubtaskById(Integer id) {
        SubTask subTask = getSubtaskById(id);
        if (subTask == null)
            return;
        Epic epic = getEpicById(subTask.getMasterId());
        epic.getSubTasksIds().remove(id);
        updateEpicStatus(epic);
        subTasks.remove(id);
    }
    public void removeEpicById(Integer id) {
        Epic epic = getEpicById(id);
        if (epic == null)
            return;
        ArrayList<Integer> epicsSubtasksIds = epic.getSubTasksIds();
        for (Integer index : epicsSubtasksIds) {
            subTasks.remove(index);
        }
        epicTasks.remove(id);
    }
}
