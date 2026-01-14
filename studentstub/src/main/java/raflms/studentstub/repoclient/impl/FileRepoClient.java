package raflms.studentstub.repoclient.impl;

import raflms.studentstub.api.ConfigFactory;
import raflms.studentstub.repoclient.StudentRepoClient;
import raflms.studentstub.restclient.ProjectFileClient;

public class FileRepoClient implements StudentRepoClient {

    private final ProjectFileClient projectFileClient;

    public FileRepoClient() {
        projectFileClient = new ProjectFileClient(ConfigFactory.createConfig().getBaseApiURL());
    }

    @Override
    public String retrieveAssignmentProject(String assigmentRepoPath, String projectRoot) {
        return projectFileClient.downloadFile(assigmentRepoPath,projectRoot);
    }

    @Override
    public boolean submitProject(String studentRepoPath, String projectRoot) {
        return false;
    }
}
