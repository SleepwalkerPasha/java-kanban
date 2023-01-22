package com.taskmanager.server;

import com.google.gson.Gson;
import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class HttpTaskServer {
    private ITaskManager tasksManager = Managers.getFileBackedTaskManager("src/com/taskmanager/resources/tasks.csv");

    private HttpClient client;

    private URI url;

    private Gson gson = new Gson();
    public void getAllRegularTasks() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getAllEpics() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getAllSubtasks() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public void getAllTasks() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void createTask(Task newTask) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void createSubTask(SubTask newTask) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void createEpic(Epic newTask) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public void deleteTaskById(String id) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteSubTaskById(String id) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteEpicById(String id) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getTaskById(String id) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getSubTaskById(String id) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/subtask/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getEpicById(String id) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/epic/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getHistory() throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();
        url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
