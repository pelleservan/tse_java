import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    public static void outputCsv(String state, String line, File outputFolderPath, File inputFile) {
        File csvFilePath = new File("");

        if (state == "True") {
            String path = inputFile.getPath();

            int lastSeparatorIndex = path.lastIndexOf(File.separator);
            if (lastSeparatorIndex != -1) {
                String newFileName = outputFolderPath + "/clean_" + path.substring(lastSeparatorIndex + 1);
                csvFilePath = new File(newFileName);
            }
        } else {
            String path = inputFile.getPath();

            int lastSeparatorIndex = path.lastIndexOf(File.separator);
            if (lastSeparatorIndex != -1) {
                String newFileName = outputFolderPath + "/durty_" + path.substring(lastSeparatorIndex + 1);
                csvFilePath = new File( newFileName);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
