package test.testing.java;

import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

abstract class TaskManagerTest<T extends ITaskManager> {

    static ITaskManager taskManager;

    @Test
    void updateTask() {
        Task newTask = new Task("fdgdg", "dfgdgf", Duration.ZERO, LocalDateTime.now());
        int taskid = taskManager.createNewTask(newTask);

        final Task updatedTask = new Task("fdgdg", "dfgdgf", Status.DONE, taskid, Duration.ZERO, LocalDateTime.now());
        taskManager.updateTask(updatedTask);

        final Task savedUpdatedTask = taskManager.getTaskById(taskid);
        Assertions.assertNotNull(savedUpdatedTask);
        Assertions.assertEquals(taskid, savedUpdatedTask.getId());
        Assertions.assertEquals(Status.DONE, savedUpdatedTask.getStatus());

        final List<Task> tasks = taskManager.getRegularTasks();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(savedUpdatedTask, tasks.get(0));
        Assertions.assertEquals(1, tasks.size());
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int epicid = taskManager.createNewEpic(epic);

        final Epic updatedTask = new Epic("fdgdg", "dfgdgf", Status.DONE, epicid, Duration.ofHours(12),
        LocalDateTime.now(), epic.getSubTasksIds());
        taskManager.updateEpic(updatedTask);

        final Epic savedUpdatedEpic = taskManager.getEpicById(epicid);
        Assertions.assertNotNull(savedUpdatedEpic);
        Assertions.assertEquals(epicid, savedUpdatedEpic.getId());
        Assertions.assertEquals(Status.DONE, savedUpdatedEpic.getStatus());

        final List<Epic> tasks = taskManager.getEpicTasks();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(savedUpdatedEpic, tasks.get(0));
        Assertions.assertEquals(1, tasks.size());
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.of(2022,1,15,12,0));
        int epicId = taskManager.createNewEpic(epic);
        SubTask newTask1 = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.of(2022,1,14,12,0), epicId);
        SubTask newTask2 = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.of(2022,1,13,12,0), epicId);
        int subtaskid1 = taskManager.createNewSubtask(newTask1);
        int subtaskid2 = taskManager.createNewSubtask(newTask2);

        final SubTask updatedSubtask1 = new SubTask("fdgdg", "dfgdgf", Status.DONE, subtaskid1,
                Duration.ZERO, LocalDateTime.of(2022,1,12,12,0), epicId);
        taskManager.updateSubtask(updatedSubtask1);

        final SubTask savedUpdatedSubtask1 = taskManager.getSubtaskById(subtaskid1);
        Assertions.assertNotNull(savedUpdatedSubtask1);
        Assertions.assertEquals(subtaskid1, savedUpdatedSubtask1.getId());
        Assertions.assertEquals(Status.DONE, savedUpdatedSubtask1.getStatus());

        final Epic savedEpic = taskManager.getEpicById(epicId);
        Assertions.assertEquals(Status.IN_PROGRESS, savedEpic.getStatus());

        final SubTask updatedSubtask2 = new SubTask("fdgdg", "dfgdgf", Status.DONE, subtaskid2,
                Duration.ZERO, LocalDateTime.now(), epicId);
        taskManager.updateSubtask(updatedSubtask2);

        final SubTask savedUpdatedSubtask2 = taskManager.getSubtaskById(subtaskid2);
        Assertions.assertNotNull(savedUpdatedSubtask2);
        Assertions.assertEquals(subtaskid2, savedUpdatedSubtask2.getId());
        Assertions.assertEquals(Status.DONE, savedUpdatedSubtask2.getStatus());

        Assertions.assertEquals(Status.DONE, savedEpic.getStatus());
    }

    @Test
    void createNewTask() {
        Task newTask = new Task("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid = taskManager.createNewTask(newTask);

        final Task savedTask = taskManager.getTaskById(taskid);

        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(newTask, savedTask);

        final List<Task> tasks = taskManager.getRegularTasks();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(newTask, tasks.get(0));
    }

    @Test
    void createNewSubtask() {
        Epic epic = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int epicId = taskManager.createNewEpic(epic);
        SubTask newTask = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.now(), epicId);
        int subtaskid = taskManager.createNewSubtask(newTask);

        final SubTask savedTask = taskManager.getSubtaskById(subtaskid);
        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(newTask, savedTask);

        final List<SubTask> tasks = taskManager.getSubtasks();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(newTask, tasks.get(0));

        final Epic master = taskManager.getEpicById(savedTask.getMasterId());

        Assertions.assertNotNull(master);
        Assertions.assertEquals(epic, master);
    }

    @Test
    void createNewEpic() {
        Epic epic = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int epicId = taskManager.createNewEpic(epic);

        final Epic savedTask = taskManager.getEpicById(epicId);

        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(epic, savedTask);

        final List<Epic> tasks = taskManager.getEpicTasks();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(1, tasks.size());
        Assertions.assertEquals(epic, tasks.get(0));
    }

    @Test
    void removeAllTasks() {
        Task newTask = new Task("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid = taskManager.createNewTask(newTask);
        Task newTask1 = new Task("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid1 = taskManager.createNewTask(newTask);
        Task newTask2 = new Task("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid2 = taskManager.createNewTask(newTask);

        taskManager.removeAllTasks();

        final List<Task> tasks = taskManager.getRegularTasks();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(0, tasks.size());
    }

    @Test
    void removeAllEpicTasks() {
        Epic newTask = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid = taskManager.createNewEpic(newTask);
        Epic newTask1 = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid1 = taskManager.createNewEpic(newTask);
        Epic newTask2 = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid2 = taskManager.createNewEpic(newTask);

        SubTask subTask = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.now(), taskid);
        int subtaskid = taskManager.createNewSubtask(subTask);

        SubTask subTask1 = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.now(), taskid);
        int subtaskid1 = taskManager.createNewSubtask(subTask1);

        taskManager.removeAllEpicTasks();

        final List<Epic> tasks = taskManager.getEpicTasks();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(0, tasks.size());

        final List<SubTask> tasks1 = taskManager.getSubtasks();
        Assertions.assertNotNull(tasks1);
        Assertions.assertEquals(0, tasks1.size());
    }

    @Test
    void removeAllSubtasks() {
        Epic newTask = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid = taskManager.createNewEpic(newTask);

        SubTask subTask = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.now(), taskid);
        int subtaskid = taskManager.createNewSubtask(subTask);

        SubTask subTask1 = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.now(), taskid);
        int subtaskid1 = taskManager.createNewSubtask(subTask1);

        taskManager.removeAllSubtasks();

        final List<SubTask> tasks1 = taskManager.getSubtasks();
        Assertions.assertNotNull(tasks1);
        Assertions.assertEquals(0, tasks1.size());

        Assertions.assertNotNull(newTask.getSubTasksIds());
        Assertions.assertEquals(0, newTask.getSubTasksIds().size());
    }

    @Test
    void removeTaskById() {
        Task newTask = new Task("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid = taskManager.createNewTask(newTask);

        taskManager.removeTaskById(taskid);

        final Task removedTask = taskManager.getTaskById(taskid);

        final List<Task> tasks = taskManager.getRegularTasks();

        Assertions.assertNull(removedTask);
        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(0, tasks.size());
    }

    @Test
    void removeSubtaskById() {
        Epic newTask = new Epic("fdgdg", "dfgdgf", Duration.ofHours(12),
                LocalDateTime.now());
        int taskid = taskManager.createNewEpic(newTask);

        SubTask subTask = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.now(), taskid);
        int subtaskid = taskManager.createNewSubtask(subTask);

        taskManager.removeSubtaskById(subtaskid);

        final SubTask removedTask = taskManager.getSubtaskById(subtaskid);

        final Epic master = taskManager.getEpicById(taskid);

        final List<SubTask> tasks = taskManager.getSubtasks();

        Assertions.assertNull(removedTask);
        Assertions.assertNotNull(master);
        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(0, tasks.size());
        Assertions.assertNotNull(master.getSubTasksIds());
        Assertions.assertEquals(0, master.getSubTasksIds().size());
    }

    @Test
    void removeEpicById() {
        Epic newTask = new Epic("fdgdg", "dfgdgf", Duration.ZERO, LocalDateTime.now());
        int taskid = taskManager.createNewEpic(newTask);

        SubTask subTask = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.now(), taskid);
        int subtaskid = taskManager.createNewSubtask(subTask);

        SubTask subTask1 = new SubTask("fdgdg", "dfgdgf",
                Duration.ZERO, LocalDateTime.now(), taskid);
        int subtaskid1 = taskManager.createNewSubtask(subTask1);

        taskManager.removeEpicById(taskid);

        final Epic master = taskManager.getEpicById(taskid);

        final SubTask masterSubTasks1 = taskManager.getSubtaskById(subtaskid);

        final SubTask masterSubTasks2 = taskManager.getSubtaskById(subtaskid1);

        Assertions.assertNull(master);
        Assertions.assertNull(masterSubTasks1);
        Assertions.assertNull(masterSubTasks2);
    }

}