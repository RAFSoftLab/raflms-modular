package raflms.teacherstub.api;

import raflms.teacherstub.config.TeacherStubConfig;

public class ConfigFactory {

    public static TeacherStubConfig createConfig(){
        return new TeacherStubConfig("http://localhost:8091");

    }
}
