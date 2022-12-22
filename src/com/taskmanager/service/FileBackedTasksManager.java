package com.taskmanager.service;

import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private String filePath;

    public FileBackedTasksManager(String path) {
        filePath = path;
    }

    public static FileBackedTasksManager loadFromFile(String filename) {
        FileBackedTasksManager manager = new FileBackedTasksManager(filename);
        List<String> info = null;
        try {
            info = manager.readFile();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        for (int i = 1; i < info.size(); i++) {
            String s = info.get(i);
            if (s.isBlank())
                break;
            Task task = manager.fromString(s);
            if (task != null) {
                if (task instanceof Epic) {
                    manager.epicTasks.put(task.getId(),(Epic) task);
                    manager.countOfTasks++;
                }
                else if (task instanceof SubTask) {
                    manager.subTasks.put(task.getId(),(SubTask) task);
                    manager.countOfTasks++;
                }
                else {
                    manager.regularTasks.put(task.getId(), task);
                    manager.countOfTasks++;
                }
            }
        }
        String historyStr = info.get(info.size() - 1);
        List<Task> tasksInHistory = manager.historyFromString(historyStr);
        for (Task task : tasksInHistory) {
            if (task instanceof Epic)
                manager.getEpicById(task.getId());
            else if (task instanceof SubTask)
                manager.getSubtaskById(task.getId());
            else
                manager.getTaskById(task.getId());
        }
        return manager;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            writer.write("id|type|name|status|description|epic|subtaskids");
            writer.newLine();
            for (Task regularTask : regularTasks.values()) {
                writer.write(regularTask.toString());
                writer.newLine();
            }
            for (Epic epicTask : epicTasks.values()) {
                writer.write(epicTask.toString());
                writer.newLine();
            }
            for (SubTask subtask : subTasks.values()) {
                writer.write(subtask.toString());
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
            return new SubTask(fields[2], fields[4], identifyStatus(fields[3]), Integer.valueOf(fields[0]), Integer.valueOf(fields[5]));
        } else if (fields[1].equals("Task")) {
            return new Task(fields[2], fields[4], identifyStatus(fields[3]), Integer.valueOf(fields[0]));
        } else {
            String subtasks = fields[fields.length - 1].substring(1,fields[fields.length - 1].length() - 1);
            String[] values = subtasks.split(", ");
            List<Integer> subtasksids = new ArrayList<>();
            for (String s : values) {
                subtasksids.add(Integer.valueOf(s));
            }
            return new Epic(fields[2], fields[4], identifyStatus(fields[3]), Integer.valueOf(fields[0]), subtasksids);
        }
    }
    // creating
    @Override
    public int createNewTask(Task task) {
        int id = super.createNewTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    @Override
    public int createNewSubtask(SubTask subTask) {
        int id = super.createNewSubtask(subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    @Override
    public int createNewEpic(Epic epic) {
        int id = super.createNewEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }
    // removes
    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllEpicTasks() {
        super.removeAllEpicTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }
    // updates
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    // get by id

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return task;
    }

    @Override
    public SubTask getSubtaskById(Integer id) {
        SubTask subTask = super.getSubtaskById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return subTask;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
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

    public static void main(String[] args) {
        String filename = "src/com/taskmanager/resources/tasks.csv";
        ITaskManager manager = Managers.getFileBackedTaskManager(filename);

        int regtaskid1 = manager.createNewTask(new Task("Почистить обувь", "Почисти обувь к приходу мамы домой"));
        int epic1 = manager.createNewEpic(new Epic("Переезд", "Подготовь все к переезду"));
        int subtask1 = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", epic1));
        int subtask2 = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", epic1));
        manager.getTaskById(regtaskid1);
        manager.getSubtaskById(subtask1);
        manager.removeAllTasks();
        System.out.println(manager.getHistory());

        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(filename);

        System.out.println(manager1.getHistory());

        System.out.println(manager1.getEpicById(epic1));

        System.out.println(manager1.getSubtaskById(subtask2));

        System.out.println(manager1.getHistory());

    }
}
