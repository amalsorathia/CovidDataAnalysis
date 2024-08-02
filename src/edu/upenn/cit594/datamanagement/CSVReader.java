package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private String filename;

    public CSVReader(String filename) {
        this.filename = filename;
    }

    public List<String[]> readCSV() throws IOException {
        List<String[]> records = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder field = new StringBuilder();
            boolean inQuotes = false;
            List<String> record = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    
                    if (inQuotes) {
                        if (c == '"') {
                            if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                                // Escaped quote
                                field.append('"');
                                i++; // Skip next quote
                            } else {
                                // End of quoted field
                                inQuotes = false;
                            }
                        } else {
                            field.append(c);
                        }
                    } else {
                        if (c == '"') {
                            inQuotes = true;
                        } else if (c == ',') {
                            // End of field
                            record.add(field.toString());
                            field.setLength(0); // Clear the StringBuilder
                        } else {
                            field.append(c);
                        }
                    }
                }
                
                // Handle end of line
                if (inQuotes) {
                    // Multi-line field
                    field.append("\n");
                } else {
                    // End of record
                    record.add(field.toString());
                    records.add(record.toArray(new String[0]));
                    record.clear();
                    field.setLength(0);
                }
            }
            
            // Handle last field if file doesn't end with newline
            if (field.length() > 0 || !record.isEmpty()) {
                record.add(field.toString());
                records.add(record.toArray(new String[0]));
            }
        }

        return records;
    }
}