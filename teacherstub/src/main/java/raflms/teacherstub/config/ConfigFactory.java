package raflms.teacherstub.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigFactory {

    private static Properties properties = loadProperties("teacherstub.properties");

    public static TeacherStubConfig createConfig(){
        return new TeacherStubConfig(properties.getProperty("baseurl.api"));
    }

    private static Properties loadProperties(String fileName) {
        Properties prop = new Properties();
        try (InputStream inputStream = ConfigFactory.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
            }
        } catch (IOException e) {
            System.err.println("Exception while loading properties: " + e.getMessage());
            return null;
        }
        return prop;
    }
}
