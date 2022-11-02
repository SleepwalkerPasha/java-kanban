package com.taskmanager.service;

import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.Collection;
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

    public Task getTaskById(Integer id) { // методы для каждого типа задач
        if (regularTasks.containsKey(id))   // Ответ на вопрос: если у каждого класса задач будет свой счетчик, то старый метод
            return regularTasks.get(id);    // мог найти несколько соответствий для одного id и выбросил бы первое совпадение
        return null;
    }

    public SubTask getSubtaskById(Integer id){
        if (subTasks.containsKey(id))
            return subTasks.get(id);
        return null;
    }

    public Epic getEpicById(Integer id){
        if (epicTasks.containsKey(id))
            return epicTasks.get(id);
        return null;
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
        regularTasks.clear();
    }

    public void removeAllEpicTasks() {
        epicTasks.clear();
        removeAllSubtasks();
    }

    public void removeAllSubtasks() {
        subTasks.clear();
    }

    public void removeById(Integer id) {
        if (regularTasks.containsKey(id))
            regularTasks.remove(id);
        else if (epicTasks.containsKey(id))
            epicTasks.remove(id);
        else if (subTasks.containsKey(id))
            subTasks.remove(id);
    }

}
