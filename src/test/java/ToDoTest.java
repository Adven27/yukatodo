import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class ToDoTest {


    TaskRepository taskRepository = mock(TaskRepository.class);
    Importer fileImporter = mock(FileImporter.class);
    private static final String FILENAME = "test.csv";
    ToDo toDoApp = new ToDo(taskRepository, fileImporter);
    List<Task> tasks;


    @Test
    public void canAddTask() {

        when(taskRepository.findAll()).thenReturn(Collections.singletonList(new Task("New task")));
        toDoApp.add("New task");
        tasks = toDoApp.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("New task", tasks.get(0).getDescription());
        verify(taskRepository).findAll();
    }

    @Test
    public void canAddSeveralTasks() {
        addTasks();
        when(taskRepository.findAll()).thenReturn(Arrays.asList(new Task("task"), new Task("task2"), new Task("task3")));
        tasks = toDoApp.getAllTasks();
        assertEquals(3, tasks.size());
        assertEquals("task", tasks.get(0).getDescription());
        assertEquals("task2", tasks.get(1).getDescription());
        assertEquals("task3", tasks.get(2).getDescription());
        verify(taskRepository, times(1)).findAll();
    }

    /*@Test
    public void shouldReturnEmptyListWhenFindFailed() {
        when(taskRepository.findAll()).thenThrow(IllegalStateException.class);
        tasks = toDoApp.getAllTasks();
        verify(taskRepository).findAll();
       // verifyNoMoreInteractions(taskRepository);
    }*/

    @Test
    public void canRemoveTask() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(new Task("Task2")));
        toDoApp.add("Task1");
        toDoApp.add("Task2");
        toDoApp.delete("Task1");
        tasks = toDoApp.getAllTasks();
        assertEquals(1, tasks.size());
        assertFalse(tasks.get(0).getDescription().equals("Task1"));
        verify(taskRepository).findAll();
        verify(taskRepository, times(1)).delete(anyObject());
    }

    @Test
    public void defaultStateShouldBeFalse() {
        when(taskRepository.findAll()).thenReturn(Collections.singletonList(new Task("Task1")));
        toDoApp.add("Task1");
        tasks = toDoApp.getAllTasks();
        assertEquals(false, tasks.get(0).getState());
        verify(taskRepository).findAll();
    }

    @Test
    public void canFindTask() {
        when(taskRepository.find("task")).thenReturn(new Task("task"));
        addTasks();
        Task task = toDoApp.getTaskByName("task");
        assertEquals("task", task.getDescription());
        verify(taskRepository).find("task");
    }

    @Test
    public void canChangeState() {
        when(taskRepository.find("Task")).thenReturn(new Task("Task"));
        toDoApp.add("Task");
        Task task = toDoApp.getTaskByName("Task");
        task.setState(true);
        task = toDoApp.getTaskByName("Task");
        assertEquals(true, task.getState());
    }

    @Test
    public void canStoreInFile() throws IOException {
        when(fileImporter.save(anyList())).thenReturn(getResourse(FILENAME));
        addTasks();
        assertEquals(
                FileUtils.readLines((File) toDoApp.exportTasks(), "UTF-8"),
                FileUtils.readLines(getResourse(FILENAME), "UTF-8")
        );
        verify(fileImporter).save(anyList());
    }

    @Test
    public void canLoadTasksFromFile() {
        when(fileImporter.load()).thenReturn(Arrays.asList(
                new Task("task"),
                new Task("task2"),
                new Task("task3")
        ));
        List<Task> test = toDoApp.importTasks();
        assertEquals(
                test,
                Arrays.asList(
                        new Task("task"),
                        new Task("task2"),
                        new Task("task3")
                )
        );
        verify(fileImporter).load();
    }


    @Test
    public void canFindCompletedTasks() {
        when(taskRepository.find("task")).thenReturn(new Task("task"));
        when(taskRepository.find("task2")).thenReturn(new Task("task2"));
        when(taskRepository.findAll()).thenReturn(Arrays.asList(new Task("task", true), new Task("task2", true)));
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
        verify(taskRepository).find("task");
        verify(taskRepository).find("task2");
        verify(taskRepository).findAll();

    }

    @Test
    public void canFindIncompleteTasks() {
        when(taskRepository.find("task")).thenReturn(new Task("task"));
        when(taskRepository.find("task2")).thenReturn(new Task("task2"));
        when(taskRepository.find("task3")).thenReturn(new Task("task3"));
        when(taskRepository.findAll()).thenReturn(Arrays.asList(new Task("task2"), new Task("task3")));

        addTasks();
        Task a = toDoApp.getTaskByName("task");
        a.setState(true);
        List<Task> expected = new ArrayList<>();
        expected.add(toDoApp.getTaskByName("task2"));
        expected.add(toDoApp.getTaskByName("task3"));
        assertEquals(expected, toDoApp.showIncompleteTasks());
        verify(taskRepository).find("task");
        verify(taskRepository).find("task2");
        verify(taskRepository).find("task3");
        verify(taskRepository).findAll();


    }

    @Test
    public void canDeleteAllTasks() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        addTasks();
        toDoApp.deleteAllTasks();
        assertTrue(0 == toDoApp.getAllTasks().size());
        verify(taskRepository, times(1)).deleteAll();
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

