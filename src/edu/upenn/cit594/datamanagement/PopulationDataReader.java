package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.PopulationData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PopulationDataReader {
    private String filename;

    public PopulationDataReader(String filename) {
        this.filename = filename;
    }

    public List<PopulationData> readPopulationData() throws IOException {
        List<PopulationData> populationDataList = new ArrayList<>();
        CSVReader csvReader = new CSVReader(filename);
        List<String[]> records = csvReader.readCSV();

        if (records.isEmpty()) {
            return populationDataList;
        }

        String[] headers = records.get(0);
        int zipCodeIndex = findColumnIndex(headers, "zip_code");
        int populationIndex = findColumnIndex(headers, "population");

        for (int i = 1; i < records.size(); i++) {
            String[] values = records.get(i);
            
            if (values.length > Math.max(zipCodeIndex, populationIndex)) {
                String zipCode = parseZipCode(values[zipCodeIndex]);
                int population = parseIntOrZero(values[populationIndex]);

                if (zipCode != null & population != 0) {
                    populationDataList.add(new PopulationData(zipCode, population));
                }
            }
        }

        return populationDataList;
    }

    public int findColumnIndex(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1; // Column not found
    }

    public String parseZipCode(String zipCode) {
        return (zipCode != null && zipCode.matches("\\d{5}")) ? zipCode : null;
    }

    public int parseIntOrZero(String value) {
        try {
        	if (value == null) {
        		return 0;
        	}
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}