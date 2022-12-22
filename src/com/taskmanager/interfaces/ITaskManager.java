package com.taskmanager.interfaces;

import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.ManagerSaveException;

import java.util.List;

public interface ITaskManager {

    Task getTaskById(Integer id) throws ManagerSaveException;

    SubTask getSubtaskById(Integer id) throws ManagerSaveException;

    Epic getEpicById(Integer id) throws ManagerSaveException;

    List<Task> getRegularTasks();

    List<Epic> getEpicTasks();

    List<SubTask> getSubtasks();

    List<SubTask> getSubtaskByEpic(Integer epicId);

    void updateTask(Task task) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    void updateSubtask(SubTask subTask) throws ManagerSaveException;

    int createNewTask(Task task) throws ManagerSaveException;

    int createNewSubtask(SubTask subTask) throws ManagerSaveException;

    int createNewEpic(Epic epic) throws ManagerSaveException;

    void removeAllTasks() throws ManagerSaveException;

    void removeAllEpicTasks() throws ManagerSaveException;

    void removeAllSubtasks() throws ManagerSaveException;

    void removeTaskById(int id) throws ManagerSaveException;

    void removeSubtaskById(int id) throws ManagerSaveException;

    void removeEpicById(int id) throws ManagerSaveException;

    List<Task> getHistory();
}
