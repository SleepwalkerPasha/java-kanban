package com.taskmanager.interfaces;

import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface ITaskManager {

    Task getTaskById(Integer id);

    SubTask getSubtaskById(Integer id);

    Epic getEpicById(Integer id);

    List<Task> getRegularTasks();

    List<Epic> getEpicTasks();

    List<SubTask> getSubtasks();

    List<SubTask> getSubtaskByEpic(Integer epicId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(SubTask subTask);

    int createNewTask(Task task);

    int createNewSubtask(SubTask subTask);

    int createNewEpic(Epic epic);

    void removeAllTasks();

    void removeAllEpicTasks();

    void removeAllSubtasks();

    void removeTaskById(int id);

    void removeSubtaskById(int id);

    void removeEpicById(int id);

    List<Task> getHistory();
}
