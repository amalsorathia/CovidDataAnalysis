package edu.upenn.cit594.studenttests;

import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.datamanagement.PropertyDataReader;
import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertyData;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

public class PropertyDataReaderTests {

    private static final String TEST_FILE = "test_properties.csv";

    @Before
    public void setUp() throws IOException {
        // Create a test CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write("zip_code,market_value,total_livable_area\n");
            writer.write("19104,500000,2000\n");
            writer.write("19104,300000,1500\n");
            writer.write("19104,invalid,1000\n");
            writer.write("invalid,600000,invalid\n");
            writer.write("19104,700000,2500\n");
        }
    }

    @Test
    public void testReadPropertyData() throws IOException {
        PropertyDataReader reader = new PropertyDataReader(TEST_FILE);
        List<PropertyData> data = reader.readPropertyData();

        assertEquals(4, data.size());

        PropertyData first = data.get(0);
        assertEquals("19104", first.getZipCode());
        assertEquals(500000.0, first.getMarketValue(), 0.01);
        assertEquals(2000.0, first.getTotalLivableArea(), 0.01);

        PropertyData second = data.get(1);
        assertEquals("19104", second.getZipCode());
        assertEquals(300000.0, second.getMarketValue(), 0.01);
        assertEquals(1500.0, second.getTotalLivableArea(), 0.01);

        PropertyData third = data.get(2);
        assertEquals("19104", third.getZipCode());
        assertEquals(0.0, third.getMarketValue(), 0.01);
        assertEquals(1000.0, third.getTotalLivableArea(), 0.01);

        PropertyData fourth = data.get(3);
        assertEquals("19104", fourth.getZipCode());
        assertEquals(700000.0, fourth.getMarketValue(), 0.01);
        assertEquals(2500.0, fourth.getTotalLivableArea(), 0.01);
    }

    @Test(expected = IOException.class)
    public void testReadPropertyDataWithInvalidFile() throws IOException {
        PropertyDataReader reader = new PropertyDataReader("invalid_file.csv");
        reader.readPropertyData();
    }

    @Test
    public void testFindColumnIndex() {
        PropertyDataReader reader = new PropertyDataReader(TEST_FILE);
        String[] headers = {"zip_code", "market_value", "total_livable_area"};

        assertEquals(0, reader.findColumnIndex(headers, "zip_code"));
        assertEquals(1, reader.findColumnIndex(headers, "market_value"));
        assertEquals(2, reader.findColumnIndex(headers, "total_livable_area"));
        assertEquals(-1, reader.findColumnIndex(headers, "non_existent_column"));
    }

    @Test
    public void testParseZipCode() {
        PropertyDataReader reader = new PropertyDataReader(TEST_FILE);

        assertEquals("19104", reader.parseZipCode("19104"));
        assertEquals("19104", reader.parseZipCode("19104-1234"));
        assertNull(reader.parseZipCode("1910"));
        assertNull(reader.parseZipCode("invalid"));
    }

    @Test
    public void testParseDoubleOrZero() {
        PropertyDataReader reader = new PropertyDataReader(TEST_FILE);

        assertEquals(500000.0, reader.parseDoubleOrZero("500000"), 0.01);
        assertEquals(0.0, reader.parseDoubleOrZero("invalid"), 0.01);
        assertEquals(0.0, reader.parseDoubleOrZero(null), 0.01);
    }
    
    @Test
    public void testData() throws IOException {
        PropertyDataReader reader = new PropertyDataReader("properties.csv");

        List<PropertyData> data = reader.readPropertyData();
        System.out.println(data.getFirst().getZipCode());
    }
    
    @Test
    public void testMultiColumnFile() throws IOException {
    	PropertyDataReader reader = new PropertyDataReader("properties.csv");
        List<PropertyData> data = reader.readPropertyData();
        
        // Test first value with quotes
        PropertyData first = data.get(0);
        assertEquals("19131", first.getZipCode());
        assertEquals(44800.0, first.getMarketValue(), 0.01);
        assertEquals(1080.0, first.getTotalLivableArea(), 0.01);
    }
}
