package raflms.studentstub.repoclient;

public interface StudentRepoClient {

    boolean retrieveAssignmentProject(String assigmentRepoPath, String projectRoot);

    boolean submitProject(String studentRepoPath, String projectRoot);
}
