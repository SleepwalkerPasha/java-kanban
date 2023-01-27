package test.testing.java;

import com.taskmanager.interfaces.IHistoryManager;
import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Task;
import com.taskmanager.service.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


class HistoryManagerTest {

    IHistoryManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    void add() {
        Task task = new Task("gfdgdfg", "dfgdgdfgd", Duration.ZERO, LocalDateTime.now());
        manager.add(task);

        final List<Task> history = manager.getHistory();
        Assertions.assertNotNull(history);
        Assertions.assertEquals(1, history.size());
        Assertions.assertEquals(task, history.get(0));
    }

    @Test
    void remove() {
        Task task = new Task("gfdgdfg", "dfgdgdfgd", Duration.ZERO, LocalDateTime.now());
        ITaskManager taskManager = Managers.getInMemoryTaskManager();
        int taskid = taskManager.createNewTask(task);
        manager.add(task);

        manager.remove(taskid);

        final List<Task> history = manager.getHistory();
        Assertions.assertNotNull(history);
        Assertions.assertEquals(0, history.size());
    }
}