import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ToDoTest {

    private static final String FILENAME = "/Users/admin/Documents/yukatodo/src/main/resources/test.csv";
    BufferedReader br = null;
    ToDo toDoApp = new ToDo();
    List<Task> tasks;
    Task task;

    public ToDoTest() throws IOException {
    }


    @Test
    public void canAddTask() {
        toDoApp.add("New task");
        tasks = toDoApp.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("New task", tasks.get(0).getDescription());

    }

    @Test
    public void canAddSeveralTasks() {
        toDoApp.add("Task1");
        toDoApp.add("Task2");
        tasks = toDoApp.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task1", tasks.get(0).getDescription());
        assertEquals("Task2", tasks.get(1).getDescription());
    }

    @Test
    public void canRemoveTask() {
        toDoApp.add("Task1");
        toDoApp.delete("Task1");
        tasks = toDoApp.getAllTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    public void defaultStateShouldBeFalse() {
        toDoApp.add("Task1");
        tasks = toDoApp.getAllTasks();
        assertEquals(false, tasks.get(0).getState());
    }

    @Test
    public void canFindTask() {
        toDoApp.add("Task1");
        toDoApp.add("Task2");
        toDoApp.add("Task3");
        task = toDoApp.getTaskByName("Task1");
        assertEquals("Task1", task.getDescription());
    }

    @Test
    public void canChangeState() {
        toDoApp.add("Task");
        task = toDoApp.getTaskByName("Task");
        task.setState(true);
        assertEquals(true, task.getState());
    }

    @Test
    public void canChangeState2() {
        toDoApp.add("Task");
        task = toDoApp.getTaskByName("Task");
        task.setState(true);
        task = toDoApp.getTaskByName("Task");
        assertEquals(true, task.getState());
    }

    @Test
    public void canStoreInFile() throws IOException {
        toDoApp.add("task");
        toDoApp.add("task2");
        toDoApp.add("task3");
        File file1 = new File(FILENAME);
        File file2 = toDoApp.savetoFile("/Users/admin/Documents/yukatodo/src/main/resources/myfile.csv");
        assertEquals(
                FileUtils.readLines(file2, "UTF-8"),
                FileUtils.readLines(file1, "UTF-8")
        );
    }

    @Test
    public void getLoadAllTasks() {
        toDoApp.add("task");
        toDoApp.add("task2");
        toDoApp.add("task3");
        tasks = toDoApp.getAllTasks();
        File file2 = toDoApp.savetoFile("/Users/admin/Documents/yukatodo/src/main/resources/myfile.csv");
        List<Task> test = toDoApp.readFromFile(file2);
        assertEquals(true, tasks.equals(test) && tasks.containsAll(test));
    }


    @Test
    public void canFindCompletedTasks() {
        toDoApp.add("task");
        toDoApp.add("task2");
        toDoApp.add("task3");
        Task a = toDoApp.getTaskByName("task");
        a.setState(true);
        Task b = toDoApp.getTaskByName("task2");
        b.setState(true);
        List<Task> test = new ArrayList<>();
        test.add(a);
        test.add(b);
        List<Task> result = toDoApp.showCompletedTask();
        assertEquals(true, test.equals(result) && result.containsAll(test));
    }

    @Test
    public void canFindIncompleteTasks() {
        toDoApp.add("task");
        toDoApp.add("task2");
        toDoApp.add("task3");
        Task a = toDoApp.getTaskByName("task");
        a.setState(true);
        List<Task> test = new ArrayList<>();
        test.add(toDoApp.getTaskByName("task2"));
        test.add(toDoApp.getTaskByName("task3"));
        List<Task> result = toDoApp.showIncompleteTasks();
        assertEquals(true, test.equals(result) && result.containsAll(test));

    }

    @Test
    public void canDeleteAllTasks() {
        toDoApp.add("task");
        toDoApp.add("task2");
        toDoApp.add("task3");
        toDoApp.deleteAllTasks();
        assertEquals(true, toDoApp.getAllTasks().isEmpty() == true);

    }
}

