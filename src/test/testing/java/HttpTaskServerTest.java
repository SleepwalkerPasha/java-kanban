package test.testing.java;

import com.google.gson.Gson;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.server.HttpTaskServer;
import com.taskmanager.service.FileBackedTasksManager;
import com.taskmanager.service.Managers;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.net.HttpURLConnection.*;

class HttpTaskServerTest {

    private static HttpTaskServer server;

    private static String filename = "src/com/taskmanager/resources/tasks.csv";

    private static HttpClient client;

    private static URI url;

    private static HttpRequest request;

    private static HttpResponse<String> response;

    private static final Gson gson = Managers.getGson();

    private static int regTaskId = 0;

    private static int epicId = 0;

    private static int subtaskId = 0;

    private static FileBackedTasksManager fileManager;
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        server = new HttpTaskServer();
        server.start();

        client = HttpClient.newHttpClient();

        // create task

        url = URI.create("http://localhost:8080/tasks/task/");
        Task task = new Task("Почистить обувь",
                "Почисти обувь к приходу мамы домой", Duration.ZERO, LocalDateTime.of(2022, 1, 13,12,0));
        String jsonTask = gson.toJson(task);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        regTaskId = Integer.parseInt(response.body());

        // create epic

        url = URI.create("http://localhost:8080/tasks/epic/");
        Epic epic = new Epic("Почистить обувь",
                "Почисти обувь к приходу мамы домой");
        String jsonEpic = gson.toJson(epic);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonEpic))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        epicId = Integer.parseInt(response.body());

        // create subtask

        url = URI.create("http://localhost:8080/tasks/subtask/");
        SubTask subTask = new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Duration.ZERO, LocalDateTime.of(2022, 1, 14,12,0), 2);
        String jsonSubTask = gson.toJson(subTask);
        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubTask))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        subtaskId = Integer.parseInt(response.body());

        // get subtask by id

        url = URI.create("http://localhost:8080/tasks/subtask/?id=" + subtaskId);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // get task by id

        url = URI.create("http://localhost:8080/tasks/task/?id=" + regTaskId);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // get epic by id

        url = URI.create("http://localhost:8080/tasks/epic/?id=" + epicId);
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void getHistoryTest() {
        url = URI.create("http://localhost:8080/tasks/history/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        fileManager = FileBackedTasksManager.load(filename);
        String jsonHistory = gson.toJson(fileManager.getHistory());

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(jsonHistory, response.body());
    }

    @Test
    void getSubtasksTest() {
        url = URI.create("http://localhost:8080/tasks/subtask/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        fileManager = FileBackedTasksManager.load(filename);
        List<SubTask> subTasks = fileManager.getSubtasks();
        String jsonSubtasks = gson.toJson(subTasks);

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(jsonSubtasks, response.body());
    }

    @Test
    void addSubtask() {
        url = URI.create("http://localhost:8080/tasks/subtask/");

        SubTask subTask = new SubTask("Подготовить машину к переезду", "Заправь машину и складируй в нее вещи",
                Status.NEW, 4, Duration.ofHours(12), LocalDateTime.of(2022, 12,12,12,0), epicId);
        String jsonSubtask = gson.toJson(subTask);

        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonSubtask))
                .build();

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(HTTP_CREATED, response.statusCode());
    }

    @Test
    void getSubtaskById() {
        url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        fileManager = FileBackedTasksManager.load(filename);
        SubTask subTask = fileManager.getSubtaskById(3);

        String jsonSubtask = gson.toJson(subTask);

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(jsonSubtask, response.body());
    }

    @Test
    void deleteSubtaskById() {
        url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(HTTP_ACCEPTED, response.statusCode());
        Assertions.assertNotNull(response.body());
    }

    @Test
    void getRegularTasksTest() {
        url = URI.create("http://localhost:8080/tasks/task/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        fileManager = FileBackedTasksManager.load(filename);
        List<Task> regularTasks = fileManager.getRegularTasks();
        String jsonTasks = gson.toJson(regularTasks);

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(jsonTasks, response.body());
    }

    @Test
    void addTask() {
        url = URI.create("http://localhost:8080/tasks/task/");

        Task task = new Task("Подготовить машину к переезду", "Заправь машину и складируй в нее вещи",
                Status.NEW, 5, Duration.ZERO, LocalDateTime.of(2024, 12,12,12,0));
        String jsonTask = gson.toJson(task);

        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(HTTP_CREATED, response.statusCode());
    }

    @Test
    void getTaskById() {
        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        fileManager = FileBackedTasksManager.load(filename);
        Task task = fileManager.getTaskById(1);
        String jsonTask = gson.toJson(task);

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(jsonTask, response.body());
    }

    @Test
    void deleteTaskById() {
        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(HTTP_ACCEPTED, response.statusCode());
        Assertions.assertNotNull(response.body());
    }

    @Test
    void getEpicsTest() {
        url = URI.create("http://localhost:8080/tasks/epic/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        fileManager = FileBackedTasksManager.load(filename);
        List<Epic> epicTasks = fileManager.getEpicTasks();
        String jsonTasks = gson.toJson(epicTasks);

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(jsonTasks, response.body());
    }

    @Test
    void addEpic() {
        url = URI.create("http://localhost:8080/tasks/epic/");

        Epic task = new Epic("Подготовить машину к переезду", "Заправь машину и складируй в нее вещи",
                Status.NEW, 6, null, null, new ArrayList<>());
        String jsonTask = gson.toJson(task);

        request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(HTTP_CREATED, response.statusCode());
    }

    @Test
    void getEpicById() {
        url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        fileManager = FileBackedTasksManager.load(filename);
        Epic epic = fileManager.getEpicById(2);
        String jsonTask = gson.toJson(epic);

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(jsonTask, response.body());
    }

    @Test
    void deleteEpicById() {
        url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(HTTP_ACCEPTED, response.statusCode());
        Assertions.assertNotNull(response.body());
    }

    @Test
    void getPrioritizedTasksTest() {
        url = URI.create("http://localhost:8080/tasks/");
        request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        fileManager = FileBackedTasksManager.load(filename);
        String jsonHistory = gson.toJson(fileManager.getPrioritizedTasks());

        Assertions.assertDoesNotThrow(() -> {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        });
        Assertions.assertEquals(HTTP_OK, response.statusCode());
        Assertions.assertNotNull(response.body());
        Assertions.assertEquals(jsonHistory, response.body());
    }
}