package test.testing.java;

import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.server.KVServer;
import com.taskmanager.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest {

    ITaskManager manager;

    KVServer server;

    @BeforeAll
    void setUp() {
        try {
            server = new KVServer();
            manager = Managers.getDefault(URI.create("http://localhost:8078"), server.getApiToken());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getByKey() {
    }

    @Test
    void save() {
    }
}