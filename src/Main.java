import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        int epicId = manager.createNewEpic(new Epic("Переезд", "Подготовь все к переезду"));

        int subTask1Id = manager.createNewSubtask(new SubTask("Собрать вещи в коробку",
                "Складируй одежду и обусь в коробки", epicId));
        int subTask2Id = manager.createNewSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", epicId));
        int regularTask1Id = manager.createNewTask(new Task("Почистить обувь", "Почисти обувь к приходу мамы домой"));

        int regularTask2Id = manager.createNewTask(new Task("Приготовить еду",
                "Папа попросил приготовить ужин по его любимому рецепту, сделай это до его прихода"));

        System.out.println(manager.getRegularTasks() + "\n");
        System.out.println(manager.getEpicTasks() + "\n");
        System.out.println(manager.getSubtasks() + "\n");

        System.out.println("Изменение объектов:");
        manager.updateTask(new Task("Сходить в магазин", "Сходить в магазин до обеда для скидки",
                regularTask1Id));

        manager.updateSubtask(new SubTask("Собрать вещи в коробку",
                "Складируй одежду и обусь в коробки", Status.DONE, subTask1Id, epicId));

        System.out.println(manager.getRegularTasks() + "\n");
        System.out.println(manager.getEpicTasks() + "\n");
        System.out.println(manager.getSubtasks() + "\n");

        manager.updateSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Status.DONE, subTask2Id, epicId));

        System.out.println(manager.getEpicTasks() + "\n");
        System.out.println(manager.getSubtasks() + "\n");

        manager.updateSubtask(new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи", Status.IN_PROGRESS, subTask2Id, epicId));
        manager.updateSubtask(new SubTask("Собрать вещи в коробку",
                "Складируй одежду и обусь в коробки", Status.IN_PROGRESS, subTask1Id, epicId));

        System.out.println(manager.getEpicTasks() + "\n");
        System.out.println(manager.getSubtasks() + "\n");

        System.out.println("Удаление некоторых задач");

        manager.removeTaskById(regularTask1Id);

        manager.removeSubtaskById(subTask2Id);
        System.out.println(manager.getSubtasks() + "\n");

        manager.removeEpicById(epicId);

        System.out.println(manager.getRegularTasks() + "\n");
        System.out.println(manager.getSubtasks() + "\n");
        System.out.println(manager.getEpicTasks() + "\n");

    }
}
