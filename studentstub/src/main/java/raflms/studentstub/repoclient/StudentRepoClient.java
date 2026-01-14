package raflms.studentstub.repoclient;

public interface StudentRepoClient {

    /**
     *
     * @param assigmentRepoPath
     * @param projectRoot
     * @return Putanju do zip fajla skinutog projekta
     */
    String retrieveAssignmentProject(String assigmentRepoPath, String projectRoot);

    boolean submitProject(String studentRepoPath, String projectRoot);
}
