package test.testing.java;

import com.taskmanager.service.Managers;
import org.junit.jupiter.api.BeforeEach;


class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

}