import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable{


    private String description;
    private boolean state;


    public Task(String description) {
        this.description = description;
        this.state = false;
    }

    public Task(String description, boolean state) {
        this.description = description;
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
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

    @Override
    public String toString() {
        return  description +
                ", " + state;
    }
}
