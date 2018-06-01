import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ToDo {

    List<Task> tasks = new ArrayList<Task>();
    private static final String COMMA_DELIMITER = ", ";
    private static final String NEW_LINE_SEPARATOR = "\n";
    File file;


    public ToDo() throws IOException {
    }


    public void add(String s) {
        tasks.add(new Task(s));
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public void delete(String s) {
        tasks.remove(new Task(s));
    }

    public Task getTaskByName(String description) {
        for (Task t : tasks) {
            if (t.getDescription().equals(description)) {
                return t;
            }
        }
        return null;
    }

    public File savetoFile(String fileName) {

        file = new File(fileName);
        try (FileWriter fr = new FileWriter(file)) {

            for (Task t : tasks) {
                fr.append(t.getDescription());
                fr.append(COMMA_DELIMITER);
                fr.append(String.valueOf(t.getState()));
                fr.append(NEW_LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return file;


        /*final CSVPrinter printer = CSVFormat.DEFAULT.print(file, Charset.defaultCharset());
        printer.printRecords(tasks);
        printer.flush();
        printer.close();
        return file;*/
    }


    public List<Task> readFromFile(File file2) {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file2))) {
            while (reader.ready()) {
                String[] str = reader.readLine().split(", ");
                tasks.add(new Task(str[0], Boolean.parseBoolean(str[1])));
            }
        } catch (Exception e) {
            System.out.println("MEOW");
        }
        return tasks;
    }

    public List<Task> showCompletedTask() {
        List<Task> test = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getState())
                test.add(t);
        }
        return test;
    }

    public List<Task> showIncompleteTasks() {
        List<Task> test = new ArrayList<>();
        for (Task t : tasks) {
            if (!t.getState())
                test.add(t);
        }
        return test;
    }

    public void deleteAllTasks() {
        tasks.clear();

    }
}

