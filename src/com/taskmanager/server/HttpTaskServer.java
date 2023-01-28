package com.taskmanager.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.taskmanager.adapter.LocalDateTimeAdapter;
import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.FileBackedTasksManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;


public class HttpTaskServer {
    public static final String filename = "src/com/taskmanager/resources/tasks.csv";
    private final FileBackedTasksManager tasksManager;

    private final HttpServer server;

    private static final int PORT = 8080;
    private URI url;

    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskServer() throws IOException {
        tasksManager = FileBackedTasksManager.load(filename);
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/", this::handleAllTasks);
        server.createContext("/tasks/task/", this::handleRegularTasks);
        server.createContext("/tasks/epic/", this::handleEpics);
        server.createContext("/tasks/subtask/", this::handleSubtasks);
        server.createContext("/tasks/history/", this::handleHistory);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    private void handleHistory(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            if (Pattern.matches("^/tasks/history/$", path)) {
                if (requestMethod.equals("GET")) {
                    List<Task> history = tasksManager.getHistory();
                    String jsonHistory = gson.toJson(history);
                    sendText(httpExchange, jsonHistory);
                    System.out.println("Отправили историю");
                } else {
                    System.out.println("Ждем GET а получили" + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            } else {
                System.out.println("Такого пути нет");
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            httpExchange.close();
        }
    }

    private void handleSubtasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getRawQuery();
            String requestMethod = httpExchange.getRequestMethod();
            Integer id = checkId(query, httpExchange);
            if (Pattern.matches("^/tasks/subtask/$", path)) {
                switch (requestMethod) {
                    case "GET":
                        if (query != null && id != null) {
                            SubTask task = tasksManager.getSubtaskById(id);
                            String jsonTask = gson.toJson(task);
                            sendText(httpExchange, jsonTask);
                            return;
                        }
                        List<SubTask> list = tasksManager.getSubtasks();
                        String jsonList = gson.toJson(list);
                        sendText(httpExchange, jsonList);
                        break;
                    case "POST":
                        String data = readText(httpExchange);
                        SubTask newTask = gson.fromJson(data, SubTask.class);
                        if (newTask.getMasterId() == null) {
                            System.out.println("Epic id пустой");
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        if (newTask.getId() == null)
                            tasksManager.createNewSubtask(newTask);
                        else
                            tasksManager.updateSubtask(newTask);
                        System.out.println("Изменили сабтаски");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        if (query != null && id != null) {
                            tasksManager.removeSubtaskById(id);
                            System.out.println("Удалили сабтаск " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    default:
                        System.out.println("Ожидаем GET POST DELETE, получили " + requestMethod);
                        httpExchange.sendResponseHeaders(405, 0);
                        break;
                }
            } else {
                System.out.println("Такого пути нет");
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            httpExchange.close();
        }
    }

    private void handleEpics(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getRawQuery();
            String requestMethod = httpExchange.getRequestMethod();
            Integer id = checkId(query, httpExchange);
            if (Pattern.matches("^/tasks/epic/$", path)) {
                switch (requestMethod) {
                    case "GET":
                        if (query != null && id != null) {
                            Epic task = tasksManager.getEpicById(id);
                            String jsonTask = gson.toJson(task);
                            sendText(httpExchange, jsonTask);
                            return;
                        }
                        List<Epic> list = tasksManager.getEpicTasks();
                        String jsonList = gson.toJson(list);
                        sendText(httpExchange, jsonList);
                        break;
                    case "POST":
                        String data = readText(httpExchange);
                        Epic newTask = null;
                        try {
                            newTask = gson.fromJson(data, Epic.class);
                        } catch (JsonSyntaxException exception) {
                            System.out.println(exception.getMessage());
                        }
                        if (newTask != null && newTask.getId() == null)
                            tasksManager.createNewEpic(newTask);
                        else
                            tasksManager.updateEpic(newTask);
                        System.out.println("Изменили эпики");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        if (query != null && id != null) {
                            tasksManager.removeEpicById(id);
                            System.out.println("Удалили эпик " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    default:
                        System.out.println("Ожидаем GET POST DELETE, получили " + requestMethod);
                        httpExchange.sendResponseHeaders(405, 0);
                        break;
                }
            } else {
                System.out.println("Такого пути нет");
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            httpExchange.close();
        }
    }

    private void handleRegularTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getRawQuery();
            String requestMethod = httpExchange.getRequestMethod();
            Integer id = checkId(query, httpExchange);
            if (Pattern.matches("^/tasks/task/$", path)) {
                switch (requestMethod) {
                    case "GET":
                        if (query != null) {
                            if (id != null) {
                                Task task = tasksManager.getTaskById(id);
                                String jsonTask = gson.toJson(task);
                                sendText(httpExchange, jsonTask);
                            }
                            return;
                        }
                        List<Task> list = tasksManager.getRegularTasks();
                        String jsonList = gson.toJson(list);
                        sendText(httpExchange, jsonList);
                        break;
                    case "POST":
                        String data = readText(httpExchange);
                        Task newTask = gson.fromJson(data, Task.class);
                        if (newTask.getId() == null)
                            tasksManager.createNewTask(newTask);
                        else
                            tasksManager.updateTask(newTask);
                        System.out.println("Изменили задачи");
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    case "DELETE":
                        if (query != null && id != null) {
                            tasksManager.removeTaskById(id);
                            System.out.println("Удалили задачу " + id);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                        break;
                    default:
                        System.out.println("Ожидаем GET POST DELETE, получили " + requestMethod);
                        httpExchange.sendResponseHeaders(405, 0);
                        break;
                }
            } else {
                System.out.println("Такого пути нет");
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            httpExchange.close();
        }
    }

    private Integer checkId(String query, HttpExchange httpExchange) throws IOException {
        Integer id = null;
        if (query != null) {
            String pathId = query.replace("id=", "");
            id = parseInt(pathId);
            if (id == -1) {
                System.out.println("Некорректный id");
                httpExchange.sendResponseHeaders(405, 0);
                return null;
            }
            if (id > tasksManager.getAllTaskCounter() || id <= 0) {
                System.out.println("задачи с таким id не найдено");
                httpExchange.sendResponseHeaders(405, 0);
                return null;
            }
        }
        return id;
    }

    private void handleAllTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            if (Pattern.matches("^/tasks/$", path)) {
                if (requestMethod.equals("GET")) {
                    Set<Task> set = tasksManager.getPrioritizedTasks();
                    String jsonList = gson.toJson(set);
                    sendText(httpExchange, jsonList);
                } else {
                    System.out.println("Ожидаем GET получили " + requestMethod);
                    httpExchange.sendResponseHeaders(405, 0);
                }
            } else {
                System.out.println("Такого пути нет");
                httpExchange.sendResponseHeaders(405, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            httpExchange.close();
        }
    }

    private int parseInt(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

}
