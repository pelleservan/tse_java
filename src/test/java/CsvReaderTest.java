import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.exceptions.CsvValidationException;

public class CsvReaderTest {

    @Test
    public void testReadFolderContent() throws IOException {

        Path tempDirectory = Files.createTempDirectory("test-directory");

        File csvFile1 = new File(tempDirectory.toFile(), "file1.csv");
        csvFile1.createNewFile();
        File csvFile2 = new File(tempDirectory.toFile(), "file2.csv");
        csvFile2.createNewFile();
        File txtFile = new File(tempDirectory.toFile(), "text.txt");
        txtFile.createNewFile();

        List<String> fileNameFormats = new ArrayList<>();
        fileNameFormats.add("users_\\d{14}\\.csv");
        fileNameFormats.add("clients_\\d{14}\\.csv");
        fileNameFormats.add("data_\\d{8}\\.csv");

        ArrayList<File> result = CsvReader.readFolderContent(tempDirectory.toFile(), fileNameFormats);

        ArrayList<File> expectedFiles = new ArrayList<>();
        expectedFiles.add(csvFile1);
        expectedFiles.add(csvFile2);
        Assert.assertEquals(expectedFiles, result);

        csvFile1.delete();
        csvFile2.delete();
        txtFile.delete();
        tempDirectory.toFile().delete();
    }

    @Test
    public void testGetHeader() throws IOException, CsvValidationException {

        File tempCsvFile = File.createTempFile("test-file", ".csv");

        List<String> lines = new ArrayList<>();
        lines.add("Header1,Header2,Header3");
        lines.add("Value1,Value2,Value3");
        lines.add("Value4,Value5,Value6");
        lines.add("Value7,Value8,Value9");
        Files.write(tempCsvFile.toPath(), lines);

        ArrayList<String> result = CsvReader.getHeader(tempCsvFile);

        ArrayList<String> expectedHeaders = new ArrayList<>();
        expectedHeaders.add("Header1");
        expectedHeaders.add("Header2");
        expectedHeaders.add("Header3");
        Assert.assertEquals(expectedHeaders, result);

        tempCsvFile.delete();
    }
}
