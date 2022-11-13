package com.taskmanager.interfaces;

import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.util.ArrayList;

public interface ITaskManager {

    Task getTaskById(Integer id);

    SubTask getSubtaskById(Integer id);

    Epic getEpicById(Integer id);

    ArrayList<Task> getRegularTasks();

    ArrayList<Epic> getEpicTasks();

    ArrayList<SubTask> getSubtasks();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subTask);

    int createNewTask(Task task);

    Integer createNewSubtask(SubTask subTask);

    int createNewEpic(Epic epic);

    void removeAllTasks();

    void removeAllEpicTasks();

    void removeAllSubtasks();

    void removeTaskById(Integer id);

    void removeSubtaskById(Integer id);

    void removeEpicById(Integer id);
}
