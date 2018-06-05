import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.builder.DataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.util.QualifiedTableName;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class DBTaskRepositoryTest {
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
    public void canFindAll() throws Exception {
        builder.newRow("TASK").with("ID", 1).with("DESCRIPTION", "task").with("STATE", false).add()
                .newRow("TASK").with("ID", 2).with("DESCRIPTION", "task2").with("STATE", false).add();
        IDataSet dataSet = builder.build();
        insert(dataSet);

        List<Task> tasks = dbTaskRepository.findAll();
        assertEquals(2, tasks.size());
        assertEquals(dataSet.getTable("TASK").getValue(0, "DESCRIPTION"), tasks.get(0).getDescription());
    }


    @Test
    public void canAddTask() throws Exception {
        insert(builder.newRow("TASK").add().build());
        dbTaskRepository.add(new Task("desc"));
        ITable actualTable = selectFrom("TASK");
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
        insert(dataSet);

        Task task = dbTaskRepository.find("task2");
        assertEquals(task.getDescription(), dataSet.getTable("TASK").getValue(1, "DESCRIPTION"));
        assertEquals(task.getState(), dataSet.getTable("TASK").getValue(1, "STATE"));

    }

    @Test
    public void shouldReturnNullIfTaskNotFound() throws Exception {
        builder.newRow("TASK").with("ID", 1).with("DESCRIPTION", "task").with("STATE", false).add()
                .newRow("TASK").with("ID", 2).with("DESCRIPTION", "task2").with("STATE", false).add()
                .newRow("TASK").with("ID", 3).with("DESCRIPTION", "task3").with("STATE", false).add();
        IDataSet dataSet = builder.build();
        insert(dataSet);

        Task task = dbTaskRepository.find("desc");

        assertNull(task);
    }


    @Test
    public void canUpdateTask() throws Exception {
        final Task expected = new Task("desc", false);
        expected.setId(1);
        builder.newRow("TASK")
                .with("ID", expected.getId())
                .with("DESCRIPTION", expected.getDescription())
                .with("STATE", expected.getState()).add();
        IDataSet dataSet = builder.build();
        insert(dataSet);

        expected.setState(true);
        expected.setDescription("new");
        dbTaskRepository.update(expected);

        ITable actualTable = selectFrom("TASK");
        assertEquals("new", actualTable.getValue(0, "DESCRIPTION"));
        assertEquals(true, actualTable.getValue(0, "STATE"));
    }

    @Test
    public void canDeleteTask() throws Exception {
        final Task expected = new Task("desc", false);
        expected.setId(2);
        builder.newRow("TASK").with("ID", 1).with("DESCRIPTION", "task").with("STATE", false).add()
                .newRow("TASK").with("ID", expected.getId()).with("DESCRIPTION", expected.getDescription()).with("STATE", expected.getState()).add()
                .newRow("TASK").with("ID", 3).with("DESCRIPTION", "task3").with("STATE", false).add();
        IDataSet dataSet = builder.build();
        insert(dataSet);

        dbTaskRepository.delete(expected);

        ITable actualTable = selectFrom("TASK", "where id=2");
        assertEquals(0, actualTable.getRowCount());
    }

    @Test
    public void canDeleteAllTasks() throws Exception {
        builder.newRow("TASK").with("ID", 1).with("DESCRIPTION", "task").with("STATE", false).add()
                .newRow("TASK").with("ID", 2).with("DESCRIPTION", "task2").with("STATE", false).add()
                .newRow("TASK").with("ID", 3).with("DESCRIPTION", "task3").with("STATE", false).add();
        IDataSet dataSet = builder.build();
        insert(dataSet);

        dbTaskRepository.deleteAll();

        ITable actualTable = selectFrom("TASK");
        assertEquals(0, actualTable.getRowCount());
    }


    private ITable selectFrom(String tableName, String where) throws Exception {
        IDatabaseConnection conn = databaseTester.getConnection();
        String qualifiedName = new QualifiedTableName(tableName, conn.getSchema()).getQualifiedName();

        String SQL = "select * from " + qualifiedName + " " + where;
        return conn.createQueryTable(qualifiedName, SQL);
    }

    private ITable selectFrom(String tableName) throws Exception {
        return selectFrom(tableName, "");
    }

    private void insert(IDataSet dataSet) throws Exception {
        databaseTester.setDataSet(dataSet);
        //действия перед вставкой dataset (сначала опустошение таблицы, а затем вставка)
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.onSetup();
    }
}
