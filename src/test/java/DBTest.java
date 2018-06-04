import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.builder.DataSetBuilder;
import org.dbunit.util.QualifiedTableName;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class DBTest {
    private static final IDatabaseTester databaseTester = getDbTester();
    DBTaskRepository dbTaskRepository = new DBTaskRepository();
    DataSetBuilder builder = getDataSetBuilder();

    private static JdbcDatabaseTester getDbTester() {
        try {
            return new JdbcDatabaseTester("org.h2.Driver", "jdbc:h2:mem:test", "root", "");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private DataSetBuilder getDataSetBuilder() {
        try {
            return new DataSetBuilder();
        } catch (DataSetException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void canReadTasks() throws Exception {

        builder.newRow("TASK").with("ID", 1).with("DESCRIPTION", "task").with("STATE", false).add()
                .newRow("TASK").with("ID", 2).with("DESCRIPTION", "task2").with("STATE", false).add();
        IDataSet dataSet = builder.build();
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();

        List<Task> tasks = dbTaskRepository.findAll();
        assertEquals(2, tasks.size());
        assertEquals(dataSet.getTable("TASK").getValue(0, "DESCRIPTION"), tasks.get(0).getDescription());
    }

    @Test
    public void canAddTask() throws Exception {
        dbTaskRepository.add(new Task("desc"));
        ITable actualTable = find("TASK");
        assertEquals(1, actualTable.getRowCount());
        assertEquals("desc", actualTable.getValue(0, "DESCRIPTION"));
        assertEquals(false, actualTable.getValue(0, "STATE"));
    }

    @Test
    public void canFindTask() throws Exception {

        builder.newRow("TASK").with("ID", 1).with("DESCRIPTION", "task").with("STATE", false).add()
                .newRow("TASK").with("ID", 2).with("DESCRIPTION", "task2").with("STATE", false).add()
                .newRow("TASK").with("ID", 3).with("DESCRIPTION", "task3").with("STATE", false).add();
        IDataSet dataSet = builder.build();
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();

        Task task = dbTaskRepository.find("task2");
        assertEquals(task.getDescription(), dataSet.getTable("TASK").getValue(1, "DESCRIPTION"));
        assertEquals(task.getState(), dataSet.getTable("TASK").getValue(1, "STATE"));

    }

    @Test
    public void canUpdateTask() throws Exception {
        dbTaskRepository.add(new Task("desc"));
        dbTaskRepository.update(1, "desc1", true);
        ITable actualTable = find("TASK");
        assertEquals("desc1", actualTable.getValue(0, "DESCRIPTION"));
        assertEquals(true, actualTable.getValue(0, "STATE"));
    }

    @Test
    public void canDeleteTask() throws Exception {
        dbTaskRepository.add(new Task("desc"));
        dbTaskRepository.add(new Task("desc1"));
        dbTaskRepository.add(new Task("desc2"));
        dbTaskRepository.delete("desc");
        ITable actualTable = find("TASK");
        assertFalse("desc2" == actualTable.getValue(1, "DESCRIPTION"));
    }

    @Test
    public void canDeleteAllTasks() throws Exception {
        dbTaskRepository.add(new Task("desc"));
        dbTaskRepository.add(new Task("desc1"));
        dbTaskRepository.add(new Task("desc2"));
        dbTaskRepository.deleteAll();
        ITable actualTable = find("TASK");
        assertEquals(0, actualTable.getRowCount());
    }

    private ITable find(String tableName, String where) throws Exception {
        IDatabaseConnection conn = databaseTester.getConnection();
        String qualifiedName = new QualifiedTableName(tableName, conn.getSchema()).getQualifiedName();

        String SQL = "select * from " + qualifiedName + " " + where;
        return conn.createQueryTable(qualifiedName, SQL);
    }

    private ITable find(String tableName) throws Exception {
        return find(tableName, "");
    }
}