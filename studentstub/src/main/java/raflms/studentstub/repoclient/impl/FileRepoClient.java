package raflms.studentstub.repoclient.impl;

import raflms.studentstub.repoclient.StudentRepoClient;

public class FileRepoClient implements StudentRepoClient {


    @Override
    public boolean retrieveAssignmentProject(String assigmentRepoPath, String projectRoot) {
        return false;
    }

    @Override
    public boolean submitProject(String studentRepoPath, String projectRoot) {
        return false;
    }
}
