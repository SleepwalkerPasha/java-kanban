package com.taskmanager.service;

import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

public class TaskToStringConverter {
    public static String convert(Task task) {
        String taskStr = "";
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            taskStr = epic.getId() + "|" + epic.getClass().toString().substring(28) + "|" + epic.getName()
                    + "|" + epic.getStatus() + "|" + epic.getDescription() + "|" + epic.getDuration() + "|" + epic.getStartTime()
                    + "|" + "|" + epic.getSubTasksIds();
        } else if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            taskStr = subTask.getId() + "|" + subTask.getClass().toString().substring(28) + "|" + subTask.getName()
                    + "|" + subTask.getStatus() + "|" + subTask.getDescription() + "|" + subTask.getDuration() + "|" + subTask.getStartTime()
                    + "|" + subTask.getMasterId();
        } else {
            taskStr = task.getId() + "|" + task.getClass().toString().substring(28) + "|" + task.getName()
                    + "|" + task.getStatus() + "|" + task.getDescription() + "|" + task.getDuration() + "|" + task.getStartTime();
        }
        return taskStr;
    }
}
