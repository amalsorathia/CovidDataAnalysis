package edu.upenn.cit594.studenttests;

import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.util.PopulationData;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class PopulationDataReaderTest {

    private static final String TEST_FILE = "test_population.csv";

    @Before
    public void setUp() throws IOException {
        // Create a test CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write("zip_code,population\n");
            writer.write("19104,40000\n");
            writer.write("19103,50000\n");
            writer.write("19104,invalid\n");
            writer.write("invalid,60000\n");
            writer.write("19102,70000\n");
        }
    }

    @Test
    public void testReadPopulationData() throws IOException {
        PopulationDataReader reader = new PopulationDataReader(TEST_FILE);
        List<PopulationData> data = reader.readPopulationData();

        assertEquals(3, data.size());

        PopulationData first = data.get(0);
        assertEquals("19104", first.getZipCode());
        assertEquals(40000, first.getPopulation());
        
        PopulationData second = data.get(1);
        assertEquals("19103", second.getZipCode());
        assertEquals(50000, second.getPopulation());
        
        PopulationData third = data.get(2);
        assertEquals("19102", third.getZipCode());
        assertEquals(70000, third.getPopulation());
        
    }

    @Test
    public void testFindColumnIndex() {
        PopulationDataReader reader = new PopulationDataReader(TEST_FILE);
        String[] headers = {"zip_code", "population"};
        assertEquals(0, reader.findColumnIndex(headers, "zip_code"));
        assertEquals(1, reader.findColumnIndex(headers, "population"));
        assertEquals(-1, reader.findColumnIndex(headers, "unknown_column"));
    }

    @Test
    public void testParseZipCode() {
        PopulationDataReader reader = new PopulationDataReader(TEST_FILE);
        assertEquals("19104", reader.parseZipCode("19104"));
        assertNull(reader.parseZipCode("1910"));
        assertNull(reader.parseZipCode("abcde"));
    }

    @Test
    public void testParseIntOrZero() {
        PopulationDataReader reader = new PopulationDataReader(TEST_FILE);
        assertEquals(50000, reader.parseIntOrZero("50000"));
        assertEquals(0, reader.parseIntOrZero("invalid"));
        assertEquals(0, reader.parseIntOrZero(null));
    }
    
    @Test
    public void testQuotedFile() throws IOException {
        PopulationDataReader reader = new PopulationDataReader("population.csv");
        List<PopulationData> data = reader.readPopulationData();
        
        // Test file with quotes
        assertEquals(49, data.size());

        PopulationData first = data.get(0);
        assertEquals("19102", first.getZipCode());
        assertEquals(7568, first.getPopulation());

        PopulationData last = data.get(data.size() - 1);
        assertEquals("19154", last.getZipCode());
        assertEquals(34681, last.getPopulation());  
    }
}
