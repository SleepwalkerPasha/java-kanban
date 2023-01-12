package test.testing.java;

import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.FileBackedTasksManager;
import com.taskmanager.service.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class FileBackedTasksManagerTest extends TaskManagerTest {

    String filename = "src/com/taskmanager/resources/tasks.csv";

    @BeforeEach
    void setUp() {
        taskManager = Managers.getFileBackedTaskManager(filename);
    }

    @Test
    void loadFromFile() {
        int regtaskid1 = taskManager.createNewTask(new Task("Почистить обувь",
                "Почисти обувь к приходу мамы домой", Duration.ZERO, LocalDateTime.now()));
        int epic1 = taskManager.createNewEpic(new Epic("Переезд", "Подготовь все к переезду", Duration.ZERO,
                LocalDateTime.now()));
        int subtask1 = taskManager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.now(), epic1));
        int subtask2 = taskManager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.now(), epic1));

        taskManager.getTaskById(regtaskid1);
        taskManager.getSubtaskById(subtask1);

        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile(filename);

        final List<Task> history = taskManager.getHistory();
        final List<Task> savedHistory = manager1.getHistory();
        final List<Task> tasks = taskManager.getRegularTasks();
        final List<Task> savedTasks = manager1.getRegularTasks();
        final List<SubTask> subtasks = taskManager.getSubtasks();
        final List<SubTask> savedSubtasks = manager1.getSubtasks();
        final List<Epic> epics = taskManager.getEpicTasks();
        final List<Epic> savedEpics = manager1.getEpicTasks();

        Assertions.assertNotNull(savedHistory);
        Assertions.assertEquals(history, savedHistory);
        Assertions.assertNotNull(savedTasks);
        Assertions.assertEquals(tasks, savedTasks);
        Assertions.assertNotNull(subtasks);
        Assertions.assertEquals(subtasks, savedSubtasks);
        Assertions.assertNotNull(epics);
        Assertions.assertEquals(epics, savedEpics);
    }

    @Test
    void historyToString() {
        FileBackedTasksManager manager = (FileBackedTasksManager)  taskManager;
        int regtaskid1 = manager.createNewTask(new Task("Почистить обувь",
                "Почисти обувь к приходу мамы домой", Duration.ZERO, LocalDateTime.now()));
        int epic1 = manager.createNewEpic(new Epic("Переезд", "Подготовь все к переезду", Duration.ZERO,
                LocalDateTime.now()));
        int subtask1 = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.now(), epic1));
        int subtask2 = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.now(), epic1));

        String history = manager.historyToString();

        Assertions.assertNotNull(history);
        Assertions.assertEquals("", history);

        manager.getTaskById(regtaskid1);
        manager.getEpicById(epic1);
        manager.getSubtaskById(subtask1);
        manager.getSubtaskById(subtask2);

        history = manager.historyToString();

        Assertions.assertNotNull(history);
        Assertions.assertEquals("1|2|3|4", history);
    }

    @Test
    void historyFromString() {
        FileBackedTasksManager manager = (FileBackedTasksManager) taskManager;
        int regtaskid1 = manager.createNewTask(new Task("Почистить обувь",
                "Почисти обувь к приходу мамы домой", Duration.ZERO, LocalDateTime.of(2022,1,11,12,0)));
        int epic1 = manager.createNewEpic(new Epic("Переезд", "Подготовь все к переезду", Duration.ZERO,
                LocalDateTime.of(2022,1,15,12,0)));
        int subtask1 = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022,1,12,12,0), epic1));
        int subtask2 = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022,1,14,12,0), epic1));

        manager.getTaskById(regtaskid1);
        manager.getEpicById(epic1);

        List<Task> historyFromString = manager.historyFromString("1|2");

        Assertions.assertNotNull(historyFromString);
        Assertions.assertEquals(regtaskid1, historyFromString.get(0).getId());
        Assertions.assertEquals(epic1, historyFromString.get(1).getId());
    }
}