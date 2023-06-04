package FILE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileMover {

    private static final Logger loggerInfo = LoggerFactory.getLogger("com.info.logging");
    private static final Logger loggerDebug = LoggerFactory.getLogger("com.debug.logging");

    public static void moveFileToDirectory(File sourceFile, File targetDirectory) {
        try {
            // Move the file to the target directory
            Path sourcePath = sourceFile.toPath();
            Path targetPath = targetDirectory.toPath().resolve(sourceFile.getName());

            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

            loggerInfo.info("File moved successfully to the target directory: " + targetPath);
        } catch (IOException e) {
            loggerDebug.error("An error occurred while moving the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}