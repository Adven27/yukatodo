import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class ToDoTest {

    private static final String FILENAME = "test.csv";
    ToDo toDoApp = new ToDo();
    List<Task> tasks;


    @Test
    public void canAddTask() {
        toDoApp.add("New task");
        tasks = toDoApp.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("New task", tasks.get(0).getDescription());

    }

    @Test
    public void canAddSeveralTasks() {
        addTasks();
        tasks = toDoApp.getAllTasks();
        assertEquals(3, tasks.size());
        assertEquals("task", tasks.get(0).getDescription());
        assertEquals("task2", tasks.get(1).getDescription());
        assertEquals("task3", tasks.get(2).getDescription());

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
        addTasks();
        Task task = toDoApp.getTaskByName("task");
        assertEquals("task", task.getDescription());
    }

    @Test
    public void canChangeState() {
        toDoApp.add("Task");
        Task task = toDoApp.getTaskByName("Task");
        task.setState(true);
        assertEquals(true, task.getState());
    }

    @Test
    public void canChangeState2() {
        toDoApp.add("Task");
        Task task = toDoApp.getTaskByName("Task");
        task.setState(true);
        task = toDoApp.getTaskByName("Task");
        assertEquals(true, task.getState());
    }

    @Test
    public void canStoreInFile() throws IOException {
        addTasks();
        assertEquals(
                FileUtils.readLines(toDoApp.savetoFile("save.csv"), "UTF-8"),
                FileUtils.readLines(getResourse(FILENAME), "UTF-8")
        );
    }

    @Test
    public void canLoadTasksFromFile() {
        List<Task> test = toDoApp.readFromFile(getResourse(FILENAME));
        assertEquals(
                test,
                Arrays.asList(
                        new Task("task"),
                        new Task("task2"),
                        new Task("task3")
                )
        );
    }


    @Test
    public void canFindCompletedTasks() {
        addTasks();
        Task a = toDoApp.getTaskByName("task");
        a.setState(true);
        Task b = toDoApp.getTaskByName("task2");
        b.setState(true);
        List<Task> expected = new ArrayList<>();
        expected.add(a);
        expected.add(b);
        List<Task> actual = toDoApp.showCompletedTask();
        assertEquals(expected, actual);
    }

    @Test
    public void canFindIncompleteTasks() {
        addTasks();
        Task a = toDoApp.getTaskByName("task");
        a.setState(true);
        List<Task> expected = new ArrayList<>();
        expected.add(toDoApp.getTaskByName("task2"));
        expected.add(toDoApp.getTaskByName("task3"));
        assertEquals(expected, toDoApp.showIncompleteTasks());

    }

    @Test
    public void canDeleteAllTasks() {
        addTasks();
        toDoApp.deleteAllTasks();
        assertEquals(true, toDoApp.getAllTasks().isEmpty());

    }

    public File getResourse(String name) {
        return new File(getClass().getClassLoader().getResource(name).getFile());
    }

    public void addTasks() {
        toDoApp.add("task");
        toDoApp.add("task2");
        toDoApp.add("task3");
    }
}

