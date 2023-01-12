package test.testing.java;

import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.service.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class EpicTest {

    private ITaskManager manager = new InMemoryTaskManager();

    private int epicId;

    @BeforeEach
    public void setup() {
        manager = new InMemoryTaskManager();
        epicId = manager.createNewEpic(new Epic("sfgsgsfd", "gfdgdgd", Duration.ZERO, LocalDateTime.now()));
    }

    @Test
    public void shouldReturnStatusNewIfEpicIsNewAndWithEmptySubtasks() {
        Assertions.assertEquals(Status.NEW, manager.getEpicById(epicId).getStatus());
    }

    @Test
    public void shouldReturnStatusNewIfEpicsSubtaskHasBeenRemoved() {
        int subtaskid1 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.now(), epicId));
        int subtaskid2 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.of(2022,1,12,12,0), epicId));

        manager.removeAllSubtasks();

        Assertions.assertEquals(Status.NEW, manager.getEpicById(epicId).getStatus());
    }

    @Test
    public void shouldReturnStatusNewIfEpicHasAllSubtasksWithStatusNew() {
        int subtaskid1 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.now(), epicId));
        int subtaskid2 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.of(2022,1,12,12,0), epicId));

        Assertions.assertEquals(Status.NEW, manager.getEpicById(epicId).getStatus());
    }

    @Test
    public void shouldReturnStatusDoneIfEpicHasAllSubtasksWithStatusDone() {
        int subtaskid1 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.now(), epicId));
        int subtaskid2 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.of(2022,1,12,12,0), epicId));

        SubTask subTask1 = manager.getSubtaskById(subtaskid1);
        SubTask subTask2 = manager.getSubtaskById(subtaskid2);
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        manager.updateSubtask(subTask1);
        manager.updateSubtask(subTask2);

        Assertions.assertEquals(Status.DONE, manager.getEpicById(epicId).getStatus());
    }

    @Test
    public void shouldReturnStatusInProgressIfEpicHasSubtasksWithStatusDoneAndStatusNew() {
        int subtaskid1 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.now(), epicId));
        int subtaskid2 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.of(2022,1,12,12,0), epicId));

        SubTask subTask1 = manager.getSubtaskById(subtaskid1);
        subTask1.setStatus(Status.DONE);
        manager.updateSubtask(subTask1);

        Assertions.assertEquals(Status.IN_PROGRESS, manager.getEpicById(epicId).getStatus());
    }

    @Test
    public void shouldReturnStatusInProgressIfEpicHasSubtasksWithStatusInProgress() {
        int subtaskid1 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.now(), epicId));
        int subtaskid2 = manager.createNewSubtask(new SubTask("sfsgfdfg", "sfgdfgd",
                Duration.ZERO, LocalDateTime.of(2022,1,12,12,0), epicId));

        SubTask subTask1 = manager.getSubtaskById(subtaskid1);
        SubTask subTask2 = manager.getSubtaskById(subtaskid2);
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subTask1);
        manager.updateSubtask(subTask2);

        Assertions.assertEquals(Status.IN_PROGRESS, manager.getEpicById(epicId).getStatus());
    }

}
