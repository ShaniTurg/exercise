import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

public class CSVHandler {
	
	private static String[] TITLES = {};

	public static List<Review> readFile(String csvFile, String[] titles) {

		// The titles from the csv file
		TITLES = titles;
		List<Review> result = new ArrayList<Review>();

		try {
			
			CsvToBean<Review> csv = new CsvToBean<Review>();
			CSVReader csvReader = new CSVReader(new FileReader(csvFile));
			result = csv.parse(setColumMapping(), csvReader);
			// Remove the titles line from the list
			result.remove(0);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	   
	private static ColumnPositionMappingStrategy<Review> setColumMapping() {
		
		ColumnPositionMappingStrategy<Review> strategy = new ColumnPositionMappingStrategy<Review>();
		strategy.setType(Review.class);
		strategy.setColumnMapping(TITLES);
		return strategy;
	}
}
