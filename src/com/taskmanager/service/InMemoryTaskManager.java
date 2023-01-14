package com.taskmanager.service;

import com.taskmanager.interfaces.IHistoryManager;
import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements ITaskManager {

    protected int counter;

    protected HashMap<Integer, Task> regularTasks;

    protected HashMap<Integer, Epic> epicTasks;

    protected HashMap<Integer, SubTask> subTasks;

    protected IHistoryManager historyManager;

    public InMemoryTaskManager() {
        this.regularTasks = new HashMap<>();
        this.epicTasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        counter = 0;
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
        for (SubTask subtask : subTasks.values()) {
            if (subtask.getMasterId().equals(epicId))
                subTaskList.add(subtask);
        }
        return subTaskList;
    }

    public int getAllTaskCounter() {
        return epicTasks.size() + regularTasks.size() + subTasks.size();
    }

    @Override
    public boolean updateTask(Task task) {
        Task oldTask = regularTasks.remove(task.getId());
        if (validateTask(task)) {
            int id = task.getId();
            if (oldTask == null)
                return false;
            regularTasks.put(id, task);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        Epic oldEpic = epicTasks.remove(epic.getId());
        if (validateTask(epic)) {
            int id = epic.getId();
            if (oldEpic == null)
                return false;
            epicTasks.put(id, epic);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSubtask(SubTask subTask) {
        SubTask oldSubtask = subTasks.remove(subTask.getId());
        if (oldSubtask == null)
            return false;
        Epic epic = epicTasks.get(oldSubtask.getMasterId());
        if (epic == null)
            return false;
        if (validateTask(subTask)) {
            int id = subTask.getId();
            subTasks.put(id, subTask);
            updateEpicStatus(epic);
            updateEpicInternals(epic);
            return true;
        }
        return false;
    }

    private void updateEpicStatus(Epic epic) {
        List<Integer> tasks = epic.getSubTasksIds();
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
        int id = -1;
        if (validateTask(task)) {
            id = ++counter;
            task.setId(id);
            regularTasks.put(id, task);
        }
        return id;
    }

    @Override
    public int createNewSubtask(SubTask subTask) {
        int id = -1;
        if (validateTask(subTask)) {
            id = ++counter;
            subTask.setId(id);
            Epic epic = epicTasks.get(subTask.getMasterId());
            if (epic == null)
                return -1;
            epic.addSubtaskId(id);
            subTasks.put(id, subTask);
            updateEpicStatus(epic);
            updateEpicInternals(epic);
        }
        return id;
    }

    @Override
    public int createNewEpic(Epic epic) {
        int id = -1;
        if (validateTask(epic)) {
            id = ++counter;
            epic.setId(id);
            epicTasks.put(id, epic);
        }
        return id;
    }

    @Override
    public void removeAllTasks() {
        if (!regularTasks.isEmpty()) {
            for (Integer taskId : regularTasks.keySet()) {
                historyManager.remove(taskId);
            }
            regularTasks.clear();
        }
    }

    @Override
    public void removeAllEpicTasks() {
        if (!epicTasks.isEmpty()) {
            for (Integer epicId : epicTasks.keySet()) {
                historyManager.remove(epicId);
            }
            removeAllSubtasks();
            epicTasks.clear();
        }
    }

    @Override
    public void removeAllSubtasks() {
        for (SubTask value : subTasks.values()) {
            Epic epic = epicTasks.get(value.getMasterId());
            if (epic == null)
                return;
            List<Integer> subTasksIds = epic.getSubTasksIds();
            subTasksIds.clear();
            updateEpicStatus(epic);
            updateEpicInternals(epic);
        }
        if (!subTasks.isEmpty()) {
            for (Integer id : subTasks.keySet()) {
                historyManager.remove(id);
            }
            subTasks.clear();
        }
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
        Epic epic = epicTasks.get(subTask.getMasterId());
        epic.getSubTasksIds().remove((Object) id);
        updateEpicStatus(epic);
        updateEpicInternals(epic);
        subTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epicTasks.get(id);
        if (epic == null)
            return;
        List<Integer> epicsSubtasksIds = epic.getSubTasksIds();
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

    private void updateEpicInternals(Epic epic) {
        if (epic == null)
            return;
        List<Integer> subtaskIds = epic.getSubTasksIds();
        List<SubTask> subtasks = new ArrayList<>();
        if (!subtaskIds.isEmpty()) {
            for (Integer integer : subtaskIds) {
                subtasks.add(subTasks.get(integer));
            }
            setStartTimeForEpic(subtasks, epic);
            setEpicDuration(subtasks, epic);
            setEndTimeForEpic(subtasks, epic);
        }
    }

    private void setEndTimeForEpic(List<SubTask> subtasks, Epic epic) {
        LocalDateTime startTime = epic.getStartTime();
        Duration epicDuration = epic.getDuration();
        LocalDateTime endTime = startTime.plus(epicDuration);
        epic.setEndTime(endTime);
    }

    private void setEpicDuration(List<SubTask> subtasks, Epic epic) {
        Duration epicDuration = subtasks.stream()
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
        epic.setDuration(epicDuration);
    }

    private void setStartTimeForEpic(List<SubTask> subtasks, Epic epic) {
        LocalDateTime startTime = subtasks
                .stream()
                .map(Task::getStartTime)
                .min((LocalDateTime::compareTo))
                .orElseThrow(NoSuchElementException::new);
        epic.setStartTime(startTime);
    }

    public Set<Task> getPrioritizedTasks() {
        Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
            if (o1.getStartTime() == null)
                return 1;
            else if (o2.getStartTime() == null)
                return -1;
            int compareResult = o1.getStartTime().compareTo(o2.getStartTime());
            if (compareResult == 0) {
                return o1.getId().compareTo(o2.getId());
            }
            return compareResult;
        });
        prioritizedTasks.addAll(regularTasks.values());
        prioritizedTasks.addAll(subTasks.values());
        return prioritizedTasks;
    }

    private boolean validateTask(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null)
            return true;
        Set<Task> tasks = getPrioritizedTasks();
        List<Interval> intervals = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Task item : tasks) {
                LocalDateTime startTime = item.getStartTime();
                LocalDateTime endTime = item.getEndTime();
                if (startTime != null && endTime != null)
                    intervals.add(new Interval(startTime, endTime));
            }
        }
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        Interval taskInterval = new Interval(startTime, endTime);
        if (startTime != null && endTime != null) {
            for (Interval interval : intervals) {
                if (!(interval.getmTo().isBefore(taskInterval.getmFrom()) || taskInterval.getmTo().isBefore(interval.getmFrom())))
                    return false;
            }
        }
        return true;
    }
}