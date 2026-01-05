package raflms.teacherstub.api;

import raflms.teacherstub.config.TeacherStubConfig;

public class ConfigFactory {

    public static TeacherStubConfig createConfig(){
        TeacherStubConfig config = new TeacherStubConfig("http://localhost:8091", "/home/bojana/raflmsgitlocalnovi");
        return config;
    }
}
