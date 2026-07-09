package raflms.studentstub.api;

import org.junit.jupiter.api.Test;
import raflms.studentstub.api.datamodel.TestWithAssignments;
import raflms.studentstub.config.ConfigFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class StudentStubServiceTest {

    private final StudentStubService studentService = new StudentStubService(ConfigFactory.createConfig());

    @Test
    public void testStartAssignemnt() throws IOException, InterruptedException {
//        boolean ok = studentService.startAssigment(
//                7,
//                "2020",
//                "RN",
//                "102",
//                "testoop",
//                "grupa1",
//                "termin1",
//                "/Users/lukamitrovic/Desktop/Doktorske/tests/rafrootproject");
        boolean ok = studentService.startAssigment(
                7,
                "2020",
                "RN",
                "101",
                "koloop",
                "grupa1",
                "termin1",
                "raf1",
                "/home/bojana/raflms/studentprojectroot");
        System.out.println(studentService.getLoggedStudentRepoPath());
        assertTrue(ok);
    }


    @Test
    public void testStartAssignementWithName() throws IOException, InterruptedException {
        boolean ok = studentService.startAssigment(
                20,
                "2025",
                "RN",
                "101",
                "Marko",
                "Markovic",
                "koloop",
                "grupa1",
                "termin1",
                "raf1",
                "/Users/lukamitrovic/Desktop/untitled");
        System.out.println(studentService.getLoggedStudentRepoPath());
        assertTrue(ok);
    }

    @Test
    public void submitAssignment(){
        studentService.setLoggedStudentRepoPath("/home/user/raflms/projectsrootdir/OOP/koloop/grupa1/termin1/studentrepos/RN-7-2020-Zika-Zikic(101)");
        studentService.setProjectRoot("/home/bojana/raflms/studentprojectroot");
        boolean ok = studentService.submitAssignment(true);
        assertTrue(ok);
    }

    @Test
    public void test_getAllTestsWithAssignemnts(){
        List<TestWithAssignments> rez = studentService.getAllTestsWithAssigmentsData();
        System.out.println(rez);
        assertFalse(rez.isEmpty());
    }

    @Test
    public void testConcurrentStudentsStartAndSubmit() {
        String[] firstNames = {"Ana", "Marko", "Nikola", "Jana", "Stefan", "Milica", "Petar", "Jovana", "Luka", "Maja"};
        String[] lastNames  = {"Jovic", "Petrovic", "Nikolic", "Ilic", "Markovic", "Popovic", "Djordjevic", "Stojanovic", "Milovanovic", "Todorovic"};

        Random random = new Random();
        AtomicBoolean anyFailed = new AtomicBoolean(false);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final int indexNumber = 200 + i + 1;
            final String firstName = firstNames[i];
            final String lastName = lastNames[i];

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    // random delay before start: 0–5 s
                    Thread.sleep(random.nextInt(5000));

                    String projectRoot = Files.createTempDirectory("student_" + indexNumber + "_").toAbsolutePath().toString();
                    StudentStubService service = new StudentStubService(ConfigFactory.createConfig());

                    boolean started = service.startAssigment(
                            indexNumber,
                            "2025",
                            "RN",
                            "101",
                            firstName,
                            lastName,
                            "koloop",
                            "grupa1",
                            "termin1",
                            "ucionica1",
                            projectRoot
                    );
                    System.out.printf("Student %s %s (%d): start=%b%n", firstName, lastName, indexNumber, started);
                    if (!started) anyFailed.set(true);

                    // random work time: 1–10 s
                    Thread.sleep(1000 + random.nextInt(9000));

                    boolean submitted = service.submitAssignment(true);
                    System.out.printf("Student %s %s (%d): submit=%b%n", firstName, lastName, indexNumber, submitted);
                    if (!submitted) anyFailed.set(true);

                } catch (Exception e) {
                    System.err.printf("Exception for student %s %s: %s%n", firstName, lastName, e.getMessage());
                    anyFailed.set(true);
                }
            });

            futures.add(future);
        }

        CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        assertTimeoutPreemptively(Duration.ofSeconds(60), all::join, "Nisu svi studenti zavrsili za 60 sekundi");
        assertFalse(anyFailed.get(), "Jedan ili vise studenata nije uspesno zavrsilo start/submit");
    }


}