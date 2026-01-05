package raflms.studentstub.api;


import raflms.studentstub.config.StudentStubConfig;

public class ConfigFactory {

    public static StudentStubConfig createConfig(){
        return new StudentStubConfig("http://localhost:8091");

    }
}
