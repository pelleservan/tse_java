import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class CsvReader{
    public static ArrayList<File> readFolderContent(File directory, List<String> fileNameFormats) {
        ArrayList<File> csvFiles = new ArrayList<>();

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && matchesFileNameFormat(file.getName(), fileNameFormats)) {
                        csvFiles.add(file);
                    }
                }
            }
        }

        return csvFiles;
    }

    private static boolean matchesFileNameFormat(String fileName, List<String> fileNameFormats) {
        for (String fileNameFormat : fileNameFormats) {
            if (fileName.matches(fileNameFormat)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<String> getHeader(File file) throws CsvValidationException {
        ArrayList<String> headers = new ArrayList<>();
        if (file.isFile() && file.getName().endsWith(".csv")) {
            try {

                FileReader filereader = new FileReader(file);

                CSVReader csvReader = new CSVReader(filereader);

                String[] headersArray = csvReader.readNext();
                Collections.addAll(headers, headersArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return headers;
    }
}



