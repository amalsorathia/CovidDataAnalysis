package edu.upenn.cit594.processor;

public class DataProcessor {

   public int getTotalPopulation(ArrayList populationData) {
	int totalSum = 0;
	for (PopulationData data: populationData) {
		totalSum += data.getPopulation();}
	return totalSum;
	}






  
  
}
