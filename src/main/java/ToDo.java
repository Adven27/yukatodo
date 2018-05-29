import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ToDo {

    List<Task> tasks = new ArrayList<Task>();


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

    public File savetoFile() throws IOException {
        File file = new File("/Users/admin/Documents/yukatodo/src/main/resources/myfile.cvs");
        final CSVPrinter printer = CSVFormat.DEFAULT.print(file, Charset.defaultCharset());
        printer.printRecords(tasks);
        printer.flush();
        printer.close();
        return file;
    }
}
