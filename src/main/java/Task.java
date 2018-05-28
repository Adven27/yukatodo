import java.util.Objects;

public class Task {


    private String description;


    private boolean state;

    public Task(String description) {
        this.description = description;
        this.state = false;
    }

    public String getDescription() {
        return description;
    }
    public boolean getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return state == task.state &&
                Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {

        return Objects.hash(description, state);
    }
}
