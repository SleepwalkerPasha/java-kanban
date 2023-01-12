import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        ITaskManager taskManager = Managers.getDefault();
        int epicId = taskManager.createNewEpic(new Epic("dfgdfg", "gdfgdgd", Duration.ofDays(4),
                null));
        int epicId1 = taskManager.createNewEpic(new Epic("dfgdfg", "gdfgdgdgd", Duration.ofDays(3),
                LocalDateTime.of(2024, 10,12, 10,0)));
        int subtask1id = taskManager.createNewSubtask(new SubTask("dfgdfg", "gdfgdgdgd", Duration.ofDays(3),
                LocalDateTime.now(), epicId));
        boolean correct = taskManager.updateSubtask(new SubTask("dfgdfdfdfg", "gdfgdgdgd", Status.NEW, subtask1id, Duration.ofDays(4),
                LocalDateTime.of(2024, 10,12, 10,0), epicId));
        System.out.println("correct = " + correct);
        int subtask3id = taskManager.createNewSubtask(new SubTask("dfgdfg", "gdfgdgdgd", Duration.ofDays(3),
                null, epicId1));
        System.out.println("subtask3id = " + subtask3id);
        int regTask = taskManager.createNewTask(new Task("sgzdfg", "gdfgdgdg", Duration.ZERO,
                LocalDateTime.of(2023, 2, 12, 14,0)));

        Set<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        System.out.println(prioritizedTasks);
    }
}