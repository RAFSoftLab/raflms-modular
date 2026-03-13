package raflms.studentstub.repoclient.impl;

import org.zeroturnaround.zip.ZipUtil;
import raflms.studentstub.config.ConfigFactory;
import raflms.studentstub.repoclient.StudentRepoClient;
import raflms.studentstub.restclient.ProjectFileClient;

import java.io.File;

public class FileRepoClient implements StudentRepoClient {

    private final ProjectFileClient projectFileClient;

    public FileRepoClient() {
        projectFileClient = new ProjectFileClient(ConfigFactory.createConfig().getBaseApiURL());
    }

    @Override
    public boolean retrieveAssignmentProject(String assigmentRepoPath, String projectRoot) {
        String assignmetFilePath = projectFileClient.downloadFile(assigmentRepoPath,projectRoot);
        File assignmentZipFile = new File(assignmetFilePath);
        ZipUtil.unpack(assignmentZipFile, new File(projectRoot));
        assignmentZipFile.delete();
        return true;
    }

    @Override
    public boolean submitAssignmentProject(String studentRepoPath, String projectRoot, Boolean isFinalSubmission) {
        String zipFilePath = projectRoot+".zip";
        File f = new File(zipFilePath);
        f.delete();

        ZipUtil.pack(new File(projectRoot), new File(zipFilePath));
        return projectFileClient.uploadFile(zipFilePath, studentRepoPath, isFinalSubmission);
    }
}
