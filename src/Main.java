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

        SubTask firstSubtask = new SubTask("Собрать вещи в коробку", "Складируй одежду и обусь в коробки",
                manager.getCountOfTasks(), epicWith2SubTasks);
        manager.createTask(firstSubtask);

        SubTask secondSubtask = new SubTask("Подготовить машину к переезду", "Заправь машину и складируй в нее вещи",
                manager.getCountOfTasks(), epicWith2SubTasks);
        manager.createTask(secondSubtask);

        epicWith2SubTasks.addSubTask(firstSubtask);
        epicWith2SubTasks.addSubTask(secondSubtask);
        Task firstRegularTask = new Task("Почистить обувь", "Почисти обувь к приходу мамы домой",
                manager.getCountOfTasks());
        manager.createTask(firstRegularTask);
        Task secondRegularTask = new Task("Приготовить еду", "Папа попросил приготовить ужин по его любимому " +
                "рецепту, сделай это до его прихода", manager.getCountOfTasks());
        manager.createTask(secondRegularTask);


        System.out.println(manager.getEpicTasks() + "\n");
        System.out.println(manager.getRegularTasks() + "\n");
        System.out.println(manager.getSubTasks() + "\n");
        System.out.println("Изменение объектов:");
        firstRegularTask.setStatus(Status.IN_PROGRESS);
        secondRegularTask.setStatus(Status.DONE);
        firstSubtask.setStatus(Status.IN_PROGRESS);
        System.out.println("firstRegularTask.Status = " + firstRegularTask.getStatus());
        System.out.println("secondRegularTask.Status = " + secondRegularTask.getStatus());
        System.out.println("firstSubtask.Status = " + firstSubtask.getStatus());
        firstSubtask.setStatus(Status.DONE);
        secondSubtask.setStatus(Status.DONE);
        System.out.println("firstSubtask.Status = " + firstSubtask.getStatus());
        System.out.println("secondSubtask.Status = " + secondSubtask.getStatus());
        System.out.println("epicWith2SubTasks.Status = " + epicWith2SubTasks.getStatus());

        manager.removeById(firstRegularTask.getId());
        manager.removeById(epicWith2SubTasks.getId());
        System.out.println("Удаление некоторых задач");
        System.out.println(manager.getRegularTasks()); // стало на одну задачу меньше
        System.out.println(manager.getEpicTasks()); // мы удалили все эпики, что были в Map поэтому он вывел пустой

    }
}
