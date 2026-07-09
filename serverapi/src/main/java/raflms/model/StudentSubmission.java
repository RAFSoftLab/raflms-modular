package raflms.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
public class StudentSubmission {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudentInfo student;

    @ManyToOne
    private Assignment assignment;

    private String repoPath; // mora da se napravi pre clone-a, moze i profesor

    private String studentGroup;

    private boolean cloned;

    private LocalDateTime taskClonedTime;

    private boolean taskSubmitted;
    private LocalDateTime taskSubmittedTime;

    private String token;

    private String classroom;

    public StudentSubmission() {
    }

    public StudentSubmission(StudentInfo student, Assignment assignment, String repoPath, String studentGroup, String token) {
        this.student = student;
        this.assignment = assignment;
        this.repoPath = repoPath;
        this.studentGroup = studentGroup;
        this.token = token;
    }

    public StudentSubmission(StudentInfo student, Assignment assignment, String repoPath, String studentGroup) {
        this.student = student;
        this.assignment = assignment;
        this.repoPath = repoPath;
        this.studentGroup = studentGroup;
    }


    public StudentSubmission(StudentInfo student, Assignment assignment, String repoPath, String studentGroup, LocalDateTime taskClonedTime) {
        this.student = student;
        this.assignment = assignment;
        this.repoPath = repoPath;
        this.studentGroup = studentGroup;
        this.taskClonedTime = taskClonedTime;
    }

    public StudentSubmission(StudentInfo student, Assignment assignment, String repoPath, String studentGroup, LocalDateTime taskClonedTime, String classroom) {
        this.student = student;
        this.assignment = assignment;
        this.repoPath = repoPath;
        this.studentGroup = studentGroup;
        this.taskClonedTime = taskClonedTime;
        this.classroom = classroom;
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

    public String getRepoPath() {
        return repoPath;
    }

    public void setRepoPath(String repoPath) {
        this.repoPath = repoPath;
    }

    public boolean isCloned() {
        return cloned;
    }

    public void setCloned(boolean cloned) {
        this.cloned = cloned;
    }

    public LocalDateTime getTaskClonedTime() {
        return taskClonedTime;
    }

    public void setTaskClonedTime(LocalDateTime taskClonedTime) {
        this.taskClonedTime = taskClonedTime;
    }

    public boolean isTaskSubmitted() {
        return taskSubmitted;
    }

    public void setTaskSubmitted(boolean taskSubmitted) {
        this.taskSubmitted = taskSubmitted;
    }

    public LocalDateTime getTaskSubmittedTime() {
        return taskSubmittedTime;
    }

    public void setTaskSubmittedTime(LocalDateTime taskSubmittedTime) {
        this.taskSubmittedTime = taskSubmittedTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
