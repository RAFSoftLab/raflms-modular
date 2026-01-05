package raflms.model;


import jakarta.persistence.*;

@Entity
public class StudentScheduledForTest {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudentInfo student;

    @ManyToOne
    private Test test;

    public StudentScheduledForTest() {
    }

    public StudentScheduledForTest(StudentInfo student, Test test) {
        this.student = student;
        this.test = test;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
