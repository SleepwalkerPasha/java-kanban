import com.taskmanager.model.Epic;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Epic epicWith2SubTasks = new Epic("Переезд", "Подготовь все к переезду",
                manager.getCountOfTasks());
        manager.createTask(epicWith2SubTasks);

        SubTask firstSubtask = new SubTask("Собрать вещи в коробку", "Складируй одежду и обусь в коробки",
                manager.getCountOfTasks(), epicWith2SubTasks);
        manager.createTask(firstSubtask);

        SubTask secondSubtask = new SubTask("Подготовить машину к переезду", "Заправь машину и складируй в нее вещи",
                manager.getCountOfTasks(), epicWith2SubTasks);
        manager.createTask(secondSubtask);

        epicWith2SubTasks.addSubTask(firstSubtask);
        epicWith2SubTasks.addSubTask(secondSubtask);
        manager.createTask(new Task("Почистить обувь", "Почисти обувь к приходу мамы домой",
                manager.getCountOfTasks()));
        manager.createTask(new Task("Приготовить еду", "Папа попросил приготовить ужин по его любимому " +
                "рецепту, сделай это до его прихода", manager.getCountOfTasks()));


        System.out.println(manager.getEpicTasks() + "\n");
        System.out.println(manager.getRegularTasks() + "\n");
        System.out.println(manager.getSubTasks());

        // todo: изменить статусы созданного объекта, проверить подзадачи в эпике
    }
}
