package raflms.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class StudentSubmission {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudentInfo student;

    @ManyToOne
    private Assignment assignment;

    private String forkPath; // mora da se napravi pre clone-a, moze i profesor

    private String studentGroup;



    private boolean cloned;

    private Timestamp taskClonedTime;

    private boolean taskSubmitted;
    private Timestamp taskSubmittedTime;

    private String token;

    public StudentSubmission() {
    }

    public StudentSubmission(StudentInfo student, Assignment assignment, String forkPath, String studentGroup, String token) {
        this.student = student;
        this.assignment = assignment;
        this.forkPath = forkPath;
        this.studentGroup = studentGroup;
        this.token = token;
    }

    public StudentSubmission(StudentInfo student, Assignment assignment, String forkPath, String studentGroup) {
        this.student = student;
        this.assignment = assignment;
        this.forkPath = forkPath;
        this.studentGroup = studentGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public String getForkPath() {
        return forkPath;
    }

    public void setForkPath(String forkPath) {
        this.forkPath = forkPath;
    }

    public boolean isCloned() {
        return cloned;
    }

    public void setCloned(boolean cloned) {
        this.cloned = cloned;
    }

    public Timestamp getTaskClonedTime() {
        return taskClonedTime;
    }

    public void setTaskClonedTime(Timestamp taskClonedTime) {
        this.taskClonedTime = taskClonedTime;
    }

    public boolean isTaskSubmitted() {
        return taskSubmitted;
    }

    public void setTaskSubmitted(boolean taskSubmitted) {
        this.taskSubmitted = taskSubmitted;
    }

    public Timestamp getTaskSubmittedTime() {
        return taskSubmittedTime;
    }

    public void setTaskSubmittedTime(Timestamp taskSubmittedTime) {
        this.taskSubmittedTime = taskSubmittedTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
