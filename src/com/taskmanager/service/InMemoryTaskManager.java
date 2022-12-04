package com.taskmanager.service;

import com.taskmanager.interfaces.IHistoryManager;
import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements ITaskManager {

    private int countOfTasks;

    private HashMap<Integer, Task> regularTasks;

    private HashMap<Integer, Epic> epicTasks;

    private HashMap<Integer, SubTask> subTasks;

    private IHistoryManager historyManager;

    public InMemoryTaskManager() {
        this.regularTasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        countOfTasks = 0;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = regularTasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubtaskById(Integer id) {
        SubTask task = subTasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic task = epicTasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public List<Task> getRegularTasks() { // изменил получения значений, вношу в массив, чтобы не получать доступ к map
        return new ArrayList<>(regularTasks.values());
    }

    @Override
    public List<Epic> getEpicTasks() { // изменил получения значений, вношу в массив, чтобы не получать доступ к map
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public List<SubTask> getSubtasks() { // изменил получения значений, вношу в массив, чтобы не получать доступ к map
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<SubTask> getSubtaskByEpic(Integer epicId) {
        List<SubTask> subTaskList = new ArrayList<>();
        for (SubTask subtask: subTasks.values()) {
            if (subtask.getMasterId().equals(epicId))
                subTaskList.add(subtask);
        }
        return subTaskList;
    }

    public int getCountOfTasks() {
        return countOfTasks;
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task oldTask = regularTasks.get(id);
        if (oldTask == null)
            return;
        regularTasks.put(id, task);
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic oldEpic = epicTasks.get(id);
        if (oldEpic == null)
            return;
        epicTasks.put(id, epic);
    }

    @Override
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
        if (tasks.isEmpty()) {
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

    @Override
    public int createNewTask(Task task) {
        int id = ++countOfTasks;
        task.setId(id);
        regularTasks.put(id, task);
        return id;
    }

    @Override
    public int createNewSubtask(SubTask subTask) {
        int id = ++countOfTasks;
        subTask.setId(id);
        Epic epic = epicTasks.get(subTask.getMasterId());
        if (epic == null)
            return -1;
        epic.addSubtaskId(id);
        subTasks.put(id, subTask);
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public int createNewEpic(Epic epic) {
        int id = ++countOfTasks;
        epic.setId(id);
        epicTasks.put(id, epic);
        return id;
    }

    @Override
    public void removeAllTasks() {
        if (!regularTasks.isEmpty())
            regularTasks.clear();
    }

    @Override
    public void removeAllEpicTasks() {
        if (!epicTasks.isEmpty()) {
            epicTasks.clear();
            removeAllSubtasks();
        }
    }

    @Override
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

    @Override
    public void removeTaskById(int id) {
        regularTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        SubTask subTask = getSubtaskById(id);
        if (subTask == null)
            return;
        Epic epic = getEpicById(subTask.getMasterId());
        epic.getSubTasksIds().remove(id);
        updateEpicStatus(epic);
        subTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = getEpicById(id);
        if (epic == null)
            return;
        ArrayList<Integer> epicsSubtasksIds = epic.getSubTasksIds();
        for (Integer index : epicsSubtasksIds) {
            subTasks.remove(index);
            historyManager.remove(index);
        }
        epicTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
