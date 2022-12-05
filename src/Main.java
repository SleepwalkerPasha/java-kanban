import com.taskmanager.interfaces.ITaskManager;
import com.taskmanager.model.Epic;
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

        int subTask3Id = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", epicId));

        int epicId1 = manager.createNewEpic(new Epic("Поселение в новый дом", "Подготовить все для переселения"));

        int regularTask1Id = manager.createNewTask(new Task("Почистить обувь", "Почисти обувь к приходу мамы домой"));

        int regularTask2Id = manager.createNewTask(new Task("Приготовить еду",
                "Папа попросил приготовить ужин по его любимому рецепту, сделай это до его прихода"));
//        manager.getEpicById(epicId);
//        manager.getTaskById(regularTask2Id);
//        manager.getTaskById(regularTask1Id);
//        manager.getSubtaskById(subTask1Id);
//        manager.getEpicById(epicId1);
//        manager.getSubtaskById(subTask3Id);
//        manager.getSubtaskById(subTask2Id);
//        System.out.println(manager.getHistory() + "\n");

        manager.getEpicById(epicId);
        manager.getTaskById(regularTask2Id);
        manager.getTaskById(regularTask1Id);
        manager.getSubtaskById(subTask1Id);
        //manager.getEpicById(epicId1);
        manager.getSubtaskById(subTask2Id);
        manager.getSubtaskById(subTask1Id);
        manager.getTaskById(regularTask1Id);
        System.out.println(manager.getHistory().size());
        System.out.println(manager.getHistory() + "\n");
        manager.removeAllSubtasks();
        //manager.removeTaskById(regularTask2Id);
        System.out.println(manager.getHistory().size());
        System.out.println(manager.getHistory() + "\n");
        //manager.removeEpicById(epicId);
        manager.removeAllEpicTasks();
        System.out.println(manager.getHistory() + "\n");
        System.out.println(manager.getHistory().size());
        manager.removeAllTasks();
        System.out.println(manager.getHistory() + "\n");
        System.out.println(manager.getHistory().size());
    }
}