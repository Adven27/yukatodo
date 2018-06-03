import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;

public class Main {

    private EntityManager entityManager = EntityManagerUtil.getEntityManager();

    public static void main(String[] args) {

        Main example = new Main();
        System.out.println("After Sucessfully insertion ");
        Student student1 = example.saveStudent("Sumith");
        Student student2 = example.saveStudent("Anoop");
        example.listStudent();
        System.out.println("After Sucessfully modification ");
        example.updateStudent(student1.getStudentId(), "Sumith Honai");
        example.updateStudent(student2.getStudentId(), "Anoop Pavanai");
        example.listStudent();
        System.out.println("After Sucessfully deletion ");
        example.deleteStudent(student2.getStudentId());
        example.listStudent();


    }

    public Student saveStudent(String studentName) {
        Student student = new Student();
        try {
            entityManager.getTransaction().begin();
            student.setStudentName(studentName);
            student = entityManager.merge(student);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
        return student;
    }

    public void listStudent() {
        try {
            entityManager.getTransaction().begin();
            @SuppressWarnings("unchecked")
            List<Student> Students = entityManager.createQuery("from Student").getResultList();
            for (Iterator<Student> iterator = Students.iterator(); iterator.hasNext(); ) {
                Student student = (Student) iterator.next();
                System.out.println(student.getStudentName());
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }

    public void updateStudent(Long studentId, String studentName) {
        try {
            entityManager.getTransaction().begin();
            Student student = (Student) entityManager.find(Student.class, studentId);
            student.setStudentName(studentName);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }

    public void deleteStudent(Long studentId) {
        try {
            entityManager.getTransaction().begin();
            Student student = (Student) entityManager.find(Student.class, studentId);
            entityManager.remove(student);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
    }
}
