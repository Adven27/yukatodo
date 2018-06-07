import java.util.List;

public interface Importer {

    Object save(List<Task> tasks);
    List<Task> load();
}
