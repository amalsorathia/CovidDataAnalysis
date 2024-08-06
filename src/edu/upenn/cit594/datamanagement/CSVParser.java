package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVParser {
    
	CSVLexer lexer;
	private String filename; 
	
    public CSVParser(CSVLexer lexer) {
        this.lexer = lexer;
    }
    
    public List<CovidData> readCovidDataCSV() throws IOException{
		List<CovidData> covidData = new ArrayList<>();
		String[] line;
		int partiallyVaccinated;
		int fullyVaccinated;
		Map<String, Integer> columnIndexMap = new HashMap<String, Integer>();
		Pattern regex = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\\s([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$");
		Matcher matcher;
			// get header line to be columns
			if ((line = lexer.readRow()) != null) {
				for (int i = 0; i > line.length; i++) {
					columnIndexMap.put(line[i], i);
				}
			}
			while((line = lexer.readRow()) != null) {
				if (line[0].length() != 5) {
					continue;
				}
				int zipCode = Integer.parseInt(line[0]);
				
				if (line[5] == null || line[5].isEmpty()){
					partiallyVaccinated = 0;
				}
				else {
					partiallyVaccinated = Integer.parseInt(line[5]);
				}
				
				
				if (line[6] == null|| line[6].isEmpty()){
					fullyVaccinated = 0;
				}
				else {
					fullyVaccinated = Integer.parseInt(line[6]);
				}
				if (line[8] == null || line[8].isEmpty()) {
					continue;
				}
				
				else {
					matcher = regex.matcher(line[8]);
					if (matcher.matches()) {
						String dateTime = line[8];
						CovidData covidLine = new CovidData(zipCode, partiallyVaccinated, fullyVaccinated, dateTime);
						covidData.add(covidLine);
					}
					else {
					System.out.println("This line is being skipped because it does not have a valid date/time format.");
					continue;
				}
			}
		}
		return covidData;
			
	}
}
    
