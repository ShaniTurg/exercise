
public class Review {
	
	private String profileName;
	private String productId;
	private String review;
	
	public Review() {
		
		this.profileName = "";
		this.productId = "";
		this.review = "";
	}
	
	/*** get & set functions ***/
	
	public void setProfileName(String profileName) { this.profileName = profileName; }
	public String getProfileName() { return profileName; }

	public void setProductId(String productId) { this.productId = productId; }
	public String getProductId() { return productId; }

	public void setReview(String review) { this.review = review; }
	public String getReview() { return review; }
	
	// Get parameter by name
	public String get(String parameter) {
		
		switch (parameter) {
			case "ProfileName":
				return getProfileName();
			case "ProductId":
				return getProductId();
			case "Review":
				return getReview();
			default:
				return "There is no such parameter"; // throw!!!!
		}
	}
}
