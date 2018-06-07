import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;



public class FileImporter implements Importer {

    private static final String COMMA_DELIMITER = ", ";
    private static final String NEW_LINE_SEPARATOR = "\n";

    File file = new File("myfile.csv");

    /**
     * Добавляем задачи в csv - файл
     *
     * @param tasks лист задач
     *
     */
    @Override
    public Object save(List<Task> tasks) {

        try (FileWriter fr = new FileWriter(file)) {

            for (Task t : tasks){
                fr.append(t.getDescription());
                fr.append(COMMA_DELIMITER);
                fr.append(String.valueOf(t.getState()));
                fr.append(NEW_LINE_SEPARATOR);
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Загружает список задач из csv - файла
     *
     * @return tasks лист задач
     */

    @Override
    public List<Task> load() {
 try {
            return Files.readAllLines(file.toPath()).stream().map(this::taskFromLine).collect(toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Task taskFromLine(String line) {
        String[] vals = line.split(",");
        return new Task(vals[0], Boolean.parseBoolean(vals[1]));
    }
}
