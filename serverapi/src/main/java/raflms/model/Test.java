package raflms.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Test {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String testName; // neformalno ime da bude jedinstveno na nivou cele baze

    private LocalDate testDate;

    @ManyToOne
    private Subject subject;

    private String testType; // kolokvijum, ispit, domaci,...

    private Boolean active;

    public Test() {
    }

    public Test(String testName, LocalDate testDate, Subject subject, String testType) {
        this.testName = testName;
        this.testDate = testDate;
        this.subject = subject;
        this.testType = testType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }


    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
