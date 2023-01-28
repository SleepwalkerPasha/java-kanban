package test.testing.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taskmanager.adapter.LocalDateTimeAdapter;
import com.taskmanager.client.HttpTaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.server.KVServer;
import com.taskmanager.service.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class HttpTaskManagerTest extends TaskManagerTest {

    private static HttpTaskManager manager;

    private static KVServer server;

    private static Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @BeforeAll
    static void setUp() {
        try {
            server = new KVServer();
            server.start();
            manager = Managers.getDefault(URI.create("http://localhost:8078"), server.getApiToken());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getByKey() throws IOException, InterruptedException {
        Task task = new Task("Почистить обувь",
                "Почисти обувь к приходу мамы домой", Duration.ZERO, LocalDateTime.of(2022, 1, 13,12,0));
        Integer regtaskid1 = manager.createNewTask(task);

        Epic epic = new Epic("Переезд", "Подготовь все к переезду", Duration.ZERO,
                null);
        Integer epic1 = manager.createNewEpic(epic);

        SubTask subtask1 = new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022, 1, 14,12,0), epic1);
        Integer subtaskid1 = manager.createNewSubtask(subtask1);

        SubTask subtask2 = new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022, 1, 15,12,0), epic1);
        Integer subtaskid2 = manager.createNewSubtask(subtask2);

        String regtaskJson = manager.getByKey(regtaskid1.toString());
        Task regTask = gson.fromJson(regtaskJson, Task.class);

        String epicJson = manager.getByKey(epic1.toString());
        Epic epic2 = gson.fromJson(epicJson, Epic.class);

        String subtask1Json = manager.getByKey(subtaskid1.toString());
        SubTask subtaskFromJson1 = gson.fromJson(subtask1Json, SubTask.class);

        String subtask2Json = manager.getByKey(subtaskid2.toString());
        SubTask subtaskFromJson2 = gson.fromJson(subtask2Json, SubTask.class);

        Assertions.assertNotNull(regTask);
        // throws
        Assertions.assertEquals(task, regTask);

        Assertions.assertNotNull(epic2);
        Assertions.assertEquals(epic, epic2);

        Assertions.assertNotNull(subtaskFromJson1);
        Assertions.assertEquals(subtask1, subtaskFromJson1);

        Assertions.assertNotNull(subtaskFromJson2);
        Assertions.assertEquals(subtask2, subtaskFromJson2);
    }

    @Test
    void save() {
        Task task = new Task("Почистить обувь",
                "Почисти обувь к приходу мамы домой", Duration.ZERO, LocalDateTime.of(2022, 1, 13,12,0));
        int regtaskid1 = manager.createNewTask(task);

        Epic epic = new Epic("Переезд", "Подготовь все к переезду", Duration.ZERO,
                null);
        int epic1 = manager.createNewEpic(epic);

        SubTask subtask1 = new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022, 1, 14,12,0), epic1);
        int subtaskid1 = manager.createNewSubtask(subtask1);

        SubTask subtask2 = new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022, 1, 15,12,0), epic1);
        int subtaskid2 = manager.createNewSubtask(subtask2);

        manager.getTaskById(regtaskid1);
        manager.getSubtaskById(subtaskid1);

        final List<Task> taskList = List.of(task);
        final List<SubTask> subTaskList = List.of(subtask1, subtask2);
        final List<Epic> epicList = List.of(epic);
        final List<Task> actualHistory = List.of(task, subtask1);

        final List<Task> tasks = manager.getRegularTasks();
        final List<SubTask> subtasks = manager.getSubtasks();
        final List<Epic> epics = manager.getEpicTasks();
        final List<Task> history = manager.getHistory();

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(tasks, taskList);

        Assertions.assertNotNull(epics);
        Assertions.assertEquals(epics, epicList);

        Assertions.assertNotNull(subtasks);
        Assertions.assertEquals(subtasks, subTaskList);

        Assertions.assertNotNull(history);
        Assertions.assertEquals(actualHistory, history);

    }
}