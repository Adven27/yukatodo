import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ToDoTest {

    ToDo a = new ToDo();
    List<Task> tasks;

    @Test
    public void canAddTask() {
        a.add("New task");
        tasks = a.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("New task", tasks.get(0).getDescription());

    }

    @Test
    public void canAddSeveralTasks() {
        a.add("Task1");
        a.add("Task2");
        tasks = a.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task1", tasks.get(0).getDescription());
        assertEquals("Task2", tasks.get(1).getDescription());
    }

    @Test
    public void canRemoveTask() {
        a.add("Task1");
        a.delete("Task1");
        tasks = a.getAllTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    public void defaultStateShouldBeFalse() {
        a.add("Task1");
        tasks = a.getAllTasks();
        assertEquals(false,tasks.get(0).getState());
    }

}
