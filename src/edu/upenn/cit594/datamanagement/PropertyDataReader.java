package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.PropertyData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PropertyDataReader {
    private String filename;

    public PropertyDataReader(String filename) {
        this.filename = filename;
    }

    public List<PropertyData> readPropertyData() throws IOException {
        List<PropertyData> propertyDataList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String[] headers = br.readLine().split(","); // Read header line
            
            // Find indices of required columns
            int zipCodeIndex = findColumnIndex(headers, "zip_code");
            int marketValueIndex = findColumnIndex(headers, "market_value");
            int livableAreaIndex = findColumnIndex(headers, "total_livable_area");

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                
                // Parse and validate data
                String zipCode = parseZipCode(values[zipCodeIndex]);
                double marketValue = parseDoubleOrZero(values[marketValueIndex]);
                double livableArea = parseDoubleOrZero(values[livableAreaIndex]);

                if (zipCode != null) {
                    propertyDataList.add(new PropertyData(zipCode, marketValue, livableArea));
                }
            }
        }

        return propertyDataList;
    }

    // Change method to private?
    public int findColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1; // Column not found
    }

    // Change method to private?
    public String parseZipCode(String zipCode) {
        // Extract first 5 digits if longer, return null if invalid
        if (zipCode == null || zipCode.length() < 5) return null;
        String fiveDigitZip = zipCode.substring(0, 5);
        return fiveDigitZip.matches("\\d{5}") ? fiveDigitZip : null;
    }

    // Change method to private?
    public double parseDoubleOrZero(String value) {
        try {
        	// Handling null values
        	if (value == null) {
        	        return 0.0;
        	    }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}