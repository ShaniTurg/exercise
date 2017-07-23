import java.util.ArrayList;
import java.util.List;

public class ActiveUsers {
	
	/*
	 * Finding 1000 most active users (profile names)
	 */
	public static ArrayList<String> activeUsers(List<Review> reviews) {
		
		ArrayList<String> activeUsers = Utils.mostPopular(reviews, "ProfileName", false);
		return activeUsers;
	}
}
