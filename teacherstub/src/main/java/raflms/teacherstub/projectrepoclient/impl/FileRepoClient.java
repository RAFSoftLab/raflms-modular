package raflms.teacherstub.projectrepoclient.impl;

import org.zeroturnaround.zip.ZipUtil;
import raflms.teacherstub.config.ConfigFactory;
import raflms.teacherstub.projectrepoclient.TeacherRepoClient;
import raflms.teacherstub.restclient.ProjectFileClient;

import java.io.File;


public class FileRepoClient implements TeacherRepoClient {

    private final ProjectFileClient projectFileClient;

    public FileRepoClient(){
        projectFileClient = new ProjectFileClient(ConfigFactory.createConfig().getBaseApiURL());
    }


    @Override
    public boolean pushAssignment(String remoteRepoPath, String assignmentProjectDir) {

        String zipFilePath = assignmentProjectDir+".zip";
        File f = new File(zipFilePath);
        f.delete();

        ZipUtil.pack(new File(assignmentProjectDir), new File(zipFilePath));
        return projectFileClient.uploadFile(zipFilePath,remoteRepoPath);
    }




}
