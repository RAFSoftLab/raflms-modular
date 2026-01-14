package raflms.studentstub.repoclient;

public interface StudentRepoClient {

    /**
     *
     * @param assigmentRepoPath
     * @param projectRoot
     * @return Putanju do zip fajla skinutog projekta
     */
    boolean retrieveAssignmentProject(String assigmentRepoPath, String projectRoot);

    boolean submitAssignmentProject(String studentRepoPath, String projectRoot, Boolean isFinalSubmission);
}
