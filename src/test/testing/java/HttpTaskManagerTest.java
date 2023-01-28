package test.testing.java;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taskmanager.client.HttpTaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.server.KVServer;
import com.taskmanager.service.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class HttpTaskManagerTest extends TaskManagerTest {

    private static KVServer server;

    private static Gson gson = Managers.getGson();

    @BeforeEach
    void setUp() {
        try {
            server = new KVServer();
            server.start();
            taskManager = Managers.getDefault(URI.create("http://localhost:8078"), server.getApiToken());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void save() throws IOException, InterruptedException {
        int regtaskid1 = taskManager.createNewTask(new Task("Почистить обувь",
                "Почисти обувь к приходу мамы домой", Duration.ZERO, LocalDateTime.of(2022, 1, 13, 12, 0)));
        int epic1 = taskManager.createNewEpic(new Epic("Переезд", "Подготовь все к переезду"));
        int subtask1 = taskManager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022, 1, 14, 12, 0), epic1));
        int subtask2 = taskManager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022, 1, 15, 12, 0), epic1));

        taskManager.getTaskById(regtaskid1);
        taskManager.getSubtaskById(subtask1);

        List<Task> expectedHistory = taskManager.getHistory();

        HttpTaskManager manager = (HttpTaskManager) taskManager;
        List<Task> historyList = manager.historyFromString(manager.getByKey("history"));
        Task regTask = gson.fromJson(manager.getByKey(String.valueOf(regtaskid1)), Task.class);
        Epic epic = gson.fromJson(manager.getByKey(String.valueOf(epic1)), Epic.class);
        SubTask subTask1 = gson.fromJson(manager.getByKey(String.valueOf(subtask1)), SubTask.class);
        SubTask subTask2 = gson.fromJson(manager.getByKey(String.valueOf(subtask2)), SubTask.class);

        Assertions.assertNotNull(historyList);
        Assertions.assertEquals(expectedHistory, historyList);

        Assertions.assertNotNull(regTask);
        Assertions.assertEquals(taskManager.getTaskById(regtaskid1), regTask);

        Assertions.assertNotNull(epic);
        Assertions.assertEquals(taskManager.getEpicById(epic1), epic);

        Assertions.assertNotNull(subTask1);
        Assertions.assertEquals(taskManager.getSubtaskById(subtask1), subTask1);

        Assertions.assertNotNull(subTask2);
        Assertions.assertEquals(taskManager.getSubtaskById(subtask2), subTask2);
    }
}