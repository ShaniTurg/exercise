import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

public final class Utils {
	
	// How many popular elements we want to find
	private static final int CONST_NUM = 1000;

	/*
	 * Fint the most popular values for parameter
	 */
	public static ArrayList<String> mostPopular(List<Review> reviews, String parameter, Boolean shouldSplit) {
		
		HashMap<String, Integer> map = createParameterCounter(reviews, parameter, shouldSplit);
		return sortMap(map.entrySet());
	}

	/*
	 * Create counter hash map. key = the value of parameter, value = how many times does it appear
	 */
	private static HashMap<String, Integer> createParameterCounter(List<Review> reviews, String parameter, Boolean shouldSplit) {
		
		HashMap<String, Integer> parameter2counter = new HashMap<String, Integer>();
		
		for (Review review : reviews) {
			String key = review.get(parameter).toLowerCase();
			// If this parameter should split
			if (shouldSplit) {
				String[] splitedStr = splitString(key);
				for (String str : splitedStr) {
					addKey(parameter2counter, str);
				}
			} else {
				addKey(parameter2counter, key);
			}
		}
		
		return parameter2counter;
	}
	
	private static String[] splitString(String str) {
		
		// Remove special characters and split it by space
		String[] splitedStr = str.replaceAll("[^a-z']+", " ").trim().split("\\s+");
		return splitedStr;
	}
	
	private static void addKey(HashMap<String, Integer> map, String key) {
		
		Integer oldValue = map.get(key);
		// if this key doesn't exist in the map
		if (oldValue == null) {
			map.put(key, 1);
		} else {
			map.put(key, oldValue + 1);
		}
	}
	
	private static ArrayList<String> sortMap(Set<Entry<String, Integer>> dataSet) {
		
		// Sort the map by the value (counter)
		PriorityQueue<Entry<String, Integer>> queue = new PriorityQueue<>(new Comparator<Entry<String, Integer>>() {
			
			public int compare(Entry<String, Integer> x1, Entry<String, Integer> x2) {
				return x1.getValue().compareTo(x2.getValue());
			}
		});
		
		Iterator<Entry<String, Integer>> dataSetIt = dataSet.iterator();
		for (int i = 0 ; dataSetIt.hasNext() ; ++i) {
			queue.add(dataSetIt.next());
			if (i >= CONST_NUM) {
				queue.remove();
			}
		}
		
		ArrayList<String> sortedKeyList = new ArrayList<String>(CONST_NUM);
		while (!queue.isEmpty()) {
			sortedKeyList.add(0, queue.poll().getKey());
		}

		return sortedKeyList;
	}
}
