package raflms.teacherstub.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUtils {

    public static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    public static void deleteDirectoryIfExists(String dirPath) {
        if (dirPath == null) {
            return;
        }
        File directory = new File(dirPath);
        deleteDirectory(directory);

    }

    public static void copyFolder(Path source, Path target) throws IOException {
        try (Stream<Path> stream = Files.walk(source)) { // Use try-with-resources for the stream
            stream.forEach(sourcePath -> {
                try {
                    Path targetPath = target.resolve(source.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        // Create directory if it does not exist
                        if (!Files.exists(targetPath)) {
                            Files.createDirectories(targetPath);
                        }
                    } else {
                        // Copy file, replacing existing ones
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to copy file " + sourcePath, e);
                }
            });
        }
    }
}


