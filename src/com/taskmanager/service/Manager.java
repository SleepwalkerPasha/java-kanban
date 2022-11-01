package com.taskmanager.service;

import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
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

    public Task getTaskById(Integer id) {
        if (regularTasks.containsKey(id))
            return regularTasks.get(id);
        else if (epicTasks.containsKey(id))
            return epicTasks.get(id);
        else if (subTasks.containsKey(id))
            return subTasks.get(id);
        return null;
    }

    public HashMap<Integer, Task> getRegularTasks() {

        return regularTasks;
    }

    public HashMap<Integer, Epic> getEpicTasks() {
        return epicTasks;
    }

    public HashMap<Integer, SubTask> getSubtasks() {
        return subTasks;
    }

    public ArrayList<SubTask> getEpicSubtask(int id) {
        ArrayList<SubTask> epicSubTasks = new ArrayList<>();
        for (SubTask value : subTasks.values()) {
            if (value.getMasterId() == id)
                epicSubTasks.add(value);
        }
        return epicSubTasks;
    }

    public int getCountOfTasks() {
        return countOfTasks;
    }

    public void updateTask(Task task) {
        int id = task.getId();
        regularTasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
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
        for (Integer task : tasks) {
            if (subTasks.get(task).getStatus() == Status.DONE)
                countDone++;
            else if (subTasks.get(task).getStatus() == Status.NEW)
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

    public ArrayList<Integer> subTasksIdsOfEpicTask(Integer id) {
        ArrayList<Integer> ids = epicTasks.get(id).getSubTasksIds();
        return ids;
    }
}
