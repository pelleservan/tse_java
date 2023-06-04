import com.opencsv.exceptions.CsvValidationException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CSV.CsvCheckFile;

public class CsvCheckFileTest {

    @Test
    public void testCheckHeader() {
        List<String> fileHeader = Arrays.asList("Header1", "Header2", "Header3");
        List<String> mandatoryHeader = Arrays.asList("Header1", "Header2", "Header3");

        boolean result = CsvCheckFile.checkHeader(fileHeader, mandatoryHeader);

        Assert.assertTrue(result);
    }

    @Test
    public void testGetDataType() {
        Class<?> result1 = CsvCheckFile.getDataType("true");
        Assert.assertEquals(Boolean.class, result1);

        Class<?> result2 = CsvCheckFile.getDataType("123");
        Assert.assertEquals(Integer.class, result2);

        Class<?> result3 = CsvCheckFile.getDataType("123456789012345");
        Assert.assertEquals(Long.class, result3);

        Class<?> result4 = CsvCheckFile.getDataType("3.14");
        Assert.assertEquals(Float.class, result4);

        Class<?> result5 = CsvCheckFile.getDataType("Hello World");
        Assert.assertEquals(String.class, result5);
    }

    @Test
    public void testValidateCSV() throws IOException, CsvValidationException {
        // Create a temporary CSV file
        File tempCsvFile = File.createTempFile("test-file", ".csv");
        List<String> lines = Arrays.asList(
                "Header1,Header2,Header3",
                "Value1,Value2,Value3",
                "Value4,Value5,Value6",
                "Value7,Value8,Value9"
        );
        Files.write(tempCsvFile.toPath(), lines);

        // Define the fileHeader, dataType, and dataPattern
        ArrayList<String> fileHeader = new ArrayList<>(Arrays.asList("Header1", "Header2", "Header3"));
        Map<String, String> dataType = new HashMap<>();
        Map<String, String> dataPattern = new HashMap<>();

        // Test the validateCSV method
        boolean result = CsvCheckFile.validateCSV(tempCsvFile, tempCsvFile.getParentFile(), fileHeader, dataType, dataPattern);

        // Assert the result
        Assert.assertTrue(result);

        // Delete the temporary CSV file
        tempCsvFile.delete();
    }
}