import java.util.ArrayList;
import java.util.List;

public class AmazonReviews {
	
	public static void main(String[] args) {
		
		final String[] titles = {"id", "productId", "userId", "profileName", "helpfulnessNumerator", "helpfulnessDenominator", "score", "time", "summary", "review"};
		List<Review> reviews = CSVHandler.readFile("/Users/shaniturgeman/Documents/workspace/Roundforest/src/Reviews.csv", titles);
		
		// Finding 1000 most active users (profile names)
		ArrayList<String> activeUsers = ActiveUsers.activeUsers(reviews);
		
		// Finding 1000 most commented food items (item ids)
		ArrayList<String> commentedItems = CommentedItems.commentedItems(reviews);
		
		// Finding 1000 most used words in the reviews
		ArrayList<String> usedWords = UsedWords.usedWords(reviews);
		
		// Translate all the reviews using Google Translate API
		ArrayList<String> translated = TranslateReviews.translateReviews("en", "fr", reviews);
	}
}
