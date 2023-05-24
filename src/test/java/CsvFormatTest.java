import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

public class CsvFormatTest {

    @Test
    public void testXlmToMap() {

        File xmlFile = new File("src/main/resources/format/define_header.xml");

        Map<String, Map<String, String>> result = CsvFormat.xlmToMap(xmlFile);

        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());

        result.values().forEach(innerMap -> Assert.assertTrue(innerMap instanceof Map));
    }

    @Test
    public void testGetMandatoryHeaders() {

        Map<String, Map<String, String>> dictionary = new HashMap<>();

        ArrayList<String> result = CsvFormat.getMandatoryHeaders(dictionary);

        ArrayList<String> expectedList = new ArrayList<>();

        assertEquals(expectedList, result);
    }
}
