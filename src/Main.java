import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taskmanager.adapter.LocalDateTimeAdapter;
import com.taskmanager.client.KVTaskClient;
import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.server.HttpTaskServer;
import com.taskmanager.server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) throws IOException {
        try {
            KVServer server = new KVServer();
            server.start();
            KVTaskClient client = new KVTaskClient(URI.create("http://localhost:8078"), server.getApiToken());
            client.put("key1", "value1");
            client.put("key2", "value2");
            client.put("key3", "value3");
            String answer1 = client.load("key1");
            System.out.println("answer1 = " + answer1);
            client.load("srts");
            client.put("", "");
            String answer2 = client.load("key2");
            System.out.println("answer2 = " + answer2);
            String answer3 = client.load("key3");
            System.out.println("answer3 = " + answer3);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}