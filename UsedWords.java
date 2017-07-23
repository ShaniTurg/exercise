import java.util.ArrayList;
import java.util.List;

public class UsedWords {
	
	/*
	 * Finding 1000 most used words in the reviews
	 */
	public static ArrayList<String> usedWords(List<Review> reviews) {
		
		ArrayList<String> usedWords = Utils.mostPopular(reviews, "Review", true);
		return usedWords;
	}
}
