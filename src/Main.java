import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.Managers;

public class Main {

    public static void main(String[] args) {
        ITaskManager manager = Managers.getDefault();// return InMemoryTaskManager

        int epicId = manager.createNewEpic(new Epic("Переезд", "Подготовь все к переезду"));

        int subTask1Id = manager.createNewSubtask(new SubTask("Собрать вещи в коробку",
                "Складируй одежду и обусь в коробки", epicId));
        int subTask2Id = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", epicId));
        int regularTask1Id = manager.createNewTask(new Task("Почистить обувь", "Почисти обувь к приходу мамы домой"));

        int regularTask2Id = manager.createNewTask(new Task("Приготовить еду",
                "Папа попросил приготовить ужин по его любимому рецепту, сделай это до его прихода"));
        manager.getTaskById(regularTask2Id);
        manager.getEpicById(epicId);
        System.out.println(manager.getHistory() + "\n");
        manager.getSubtaskById(subTask1Id);
        System.out.println(manager.getHistory() + "\n");
        manager.getTaskById(regularTask2Id);
        System.out.println(manager.getHistory() + "\n");
        manager.getTaskById(regularTask2Id);
        manager.getTaskById(regularTask2Id);
        manager.getTaskById(regularTask2Id);
        manager.getTaskById(regularTask2Id);
        manager.getTaskById(regularTask2Id);
        manager.getTaskById(regularTask2Id);
        manager.getTaskById(regularTask2Id);
        System.out.println(manager.getHistory() + "\n");
        manager.getTaskById(regularTask2Id);
        System.out.println(manager.getHistory() + "\n");
        System.out.println(manager.getHistory().size());
    }
}
