import com.taskmanager.model.Epic;
import com.taskmanager.model.Status;
import com.taskmanager.model.SubTask;
import com.taskmanager.model.Task;
import com.taskmanager.service.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Epic epicWith2SubTasks = new Epic("Переезд", "Подготовь все к переезду",
                manager.getCountOfTasks());
        manager.createTask(epicWith2SubTasks);

        SubTask firstSubtask = new SubTask("Собрать вещи в коробку",
                "Складируй одежду и обусь в коробки",
                manager.getCountOfTasks(), epicWith2SubTasks);
        manager.createTask(firstSubtask);

        SubTask secondSubtask = new SubTask("Подготовить машину к переезду",
                "Заправь машину и складируй в нее вещи",
                manager.getCountOfTasks(), epicWith2SubTasks);
        manager.createTask(secondSubtask);

        epicWith2SubTasks.addSubTask(firstSubtask);
        epicWith2SubTasks.addSubTask(secondSubtask);
        Task firstRegularTask = new Task("Почистить обувь", "Почисти обувь к приходу мамы домой",
                manager.getCountOfTasks());
        manager.createTask(firstRegularTask);
        Task secondRegularTask = new Task("Приготовить еду",
                "Папа попросил приготовить ужин по его любимому рецепту, сделай это до его прихода",
                manager.getCountOfTasks());
        manager.createTask(secondRegularTask);
        System.out.println(manager.getEpicTasks() + "\n");
        System.out.println(manager.getRegularTasks() + "\n");
        System.out.println(manager.getSubTasks() + "\n");

        System.out.println("Изменение объектов:");

        manager.updateTask(new Task(firstRegularTask.getName(), firstRegularTask.getDescription(),
                firstRegularTask.getId(),
                Status.DONE));

        System.out.println(manager.getRegularTasks() + "\n");

        SubTask newFirstSubTask = new SubTask(firstSubtask.getName(), firstSubtask.getDescription(),
                firstSubtask.getId(),
                firstSubtask.getMaster());
        newFirstSubTask.setStatus(Status.DONE);
        manager.updateTask(newFirstSubTask);
        System.out.println(manager.getEpicTasks() + "\n");

        SubTask newSecondSubtask = new SubTask(secondSubtask.getName(), secondSubtask.getDescription(),
                secondSubtask.getId(),
                secondSubtask.getMaster());
        newSecondSubtask.setStatus(Status.DONE);
        manager.updateTask(newSecondSubtask);
        System.out.println(manager.getEpicTasks() + "\n");

        System.out.println("Удаление некоторых задач");

        manager.removeById(firstRegularTask.getId());
        manager.removeById(epicWith2SubTasks.getId());
        System.out.println(manager.getRegularTasks()); // стало на одну задачу меньше
        System.out.println(manager.getEpicTasks()); // мы удалили все эпики, что были в Map поэтому он вывел пустой

    }
}
