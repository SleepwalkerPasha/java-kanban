package com.taskmanager.service;

import com.taskmanager.exception.ManagerSaveException;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private String filePath;

    public FileBackedTasksManager() {

    }

    public FileBackedTasksManager(String path) {
        filePath = path;
    }

    public static FileBackedTasksManager load(String filename) {
        FileBackedTasksManager manager = new FileBackedTasksManager(filename);
        List<String> info;
        try {
            info = manager.readFile();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        int maxId = 0;
        for (int i = 1; i < info.size(); i++) {
            String s = info.get(i);
            if (s.isBlank())
                break;
            Task task = manager.fromString(s);
            if (task != null) {
                String taskString = task.toString();
                Integer taskId = task.getId();
                if (taskString.contains("Epic")) {
                    manager.epicTasks.put(taskId, (Epic) task);
                } else if (taskString.contains("SubTask")) {
                    manager.subTasks.put(taskId, (SubTask) task);
                } else {
                    manager.regularTasks.put(taskId, task);
                }
                if (taskId > maxId)
                    maxId = taskId;
            }
        }
        manager.counter = maxId;
        String historyStr = info.get(info.size() - 1);
        List<Task> tasksInHistory;
        if (!historyStr.isBlank()) {
            tasksInHistory = manager.historyFromString(historyStr);
            for (Task task : tasksInHistory) {
                manager.historyManager.add(task);
            }
        }
        return manager;
    }

    public void save() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id|type|name|status|description|duration|starttime|epic|subtaskids");
            writer.newLine();
            for (Task regularTask : regularTasks.values()) {
                writer.write(TaskToStringConverter.convert(regularTask));
                writer.newLine();
            }
            for (Epic epicTask : epicTasks.values()) {
                writer.write(TaskToStringConverter.convert(epicTask));
                writer.newLine();
            }
            for (SubTask subtask : subTasks.values()) {
                writer.write(TaskToStringConverter.convert(subtask));
                writer.newLine();
            }
            writer.newLine();
            writer.write(historyToString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    private List<String> readFile() throws IOException {
        return Files.readAllLines(Path.of(filePath));
    }

    private Status identifyStatus(String statusName) {
        switch (statusName) {
            case "NEW":
                return Status.NEW;
            case "IN_PROGRESS":
                return Status.IN_PROGRESS;
            case "DONE":
                return Status.DONE;
        }
        return null;
    }

    private Task fromString(String value) {
        if (value.isBlank())
            return null;
        String[] fields = value.split("\\|");
        if (fields[1].equals("SubTask")) {
            return new SubTask(fields[2], fields[4], identifyStatus(fields[3]), Integer.valueOf(fields[0]),
                    Duration.parse(fields[5]), LocalDateTime.parse(fields[6]), Integer.valueOf(fields[7]));
        } else if (fields[1].equals("Task")) {
            return new Task(fields[2], fields[4], identifyStatus(fields[3]), Integer.valueOf(fields[0]),
                    Duration.parse(fields[5]), LocalDateTime.parse(fields[6]));
        } else {
            String subtasks = fields[fields.length - 1].substring(1, fields[fields.length - 1].length() - 1);
            List<Integer> subtasksids = new ArrayList<>();
            if (!subtasks.equals("")) {
                String[] values = subtasks.split(", ");
                for (String s : values) {
                    subtasksids.add(Integer.valueOf(s));
                }
            }
            return new Epic(fields[2], fields[4], identifyStatus(fields[3]), Integer.valueOf(fields[0]),
                    Duration.parse(fields[5]), LocalDateTime.parse(fields[6]), subtasksids);
        }
    }

    // creating
    @Override
    public int createNewTask(Task task) {
        int id = super.createNewTask(task);
        save();
        return id;
    }

    @Override
    public int createNewSubtask(SubTask subTask) {
        int id = super.createNewSubtask(subTask);
        save();
        return id;
    }

    @Override
    public int createNewEpic(Epic epic) {
        int id = super.createNewEpic(epic);
        save();
        return id;
    }

    // removes
    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpicTasks() {
        super.removeAllEpicTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    // updates
    @Override
    public boolean updateTask(Task task) {
        boolean value = super.updateTask(task);
        save();
        return value;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean value = super.updateEpic(epic);
        save();
        return value;
    }

    @Override
    public boolean updateSubtask(SubTask subTask) {
        boolean value = super.updateSubtask(subTask);
        save();
        return value;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public SubTask getSubtaskById(Integer id) {
        SubTask subTask = super.getSubtaskById(id);
        save();
        return subTask;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    public String historyToString() {
        List<Task> tasksInHistory = getHistory();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasksInHistory.size(); i++) {
            sb.append(tasksInHistory.get(i).getId().toString());
            if (i != tasksInHistory.size() - 1)
                sb.append("|");
        }
        return sb.toString();
    }

    public List<Task> historyFromString(String value) {
        List<Task> history = new ArrayList<>();
        String[] taskids = value.split("\\|");
        for (String taskid : taskids) {
            Task task = regularTasks.get(Integer.valueOf(taskid));
            Epic epic = epicTasks.get(Integer.valueOf(taskid));
            SubTask subTask = subTasks.get(Integer.valueOf(taskid));
            if (task != null)
                history.add(task);
            else if (epic != null)
                history.add(epic);
            else if (subTask != null)
                history.add(subTask);
        }
        return history;
    }
}
