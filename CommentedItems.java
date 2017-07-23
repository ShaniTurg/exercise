import java.util.ArrayList;
import java.util.List;

public class CommentedItems {
	
	/*
	 *  Finding 1000 most commented food items (item ids)
	 */
	public static ArrayList<String> commentedItems(List<Review> reviews) {
		
		ArrayList<String> commentedItems = Utils.mostPopular(reviews, "ProductId", false);
		return commentedItems;
	}
}
