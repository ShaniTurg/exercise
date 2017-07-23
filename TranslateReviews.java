import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class TranslateReviews {

	private static ArrayList<String> ALL_REVIEWS, TRANSLATED_REVIEWS;
	private static String INPUT_LANG, OUTPUT_LANG;
	private static final int MAX_LENGTH = 1000;

	public static ArrayList<String> translateReviews(String translateFrom, String translateTo, List<Review> reviews) {

		INPUT_LANG = translateFrom;
		OUTPUT_LANG = translateTo;

		getAllReviewsText(reviews);
		sortReviewsByLength();
		
		MultiHttpCalls multiHttp = new MultiHttpCalls();
		
		try {
			
			while (multiHttp.httpPostThread.isAlive()) {
				multiHttp.run();
				Thread.sleep(200);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return TRANSLATED_REVIEWS;
	}

	private static void getAllReviewsText(List<Review> reviews) {

		// List of all the reviews
		ALL_REVIEWS = new ArrayList<String>();
		
		for (Review review : reviews) {
			String text = review.getReview();
			
			// If the  review is too long, cut it
			if (text.length() > MAX_LENGTH) {
				for (String str : cutText(text)) {
					addText(str);
				}
			} else {
				addText(text);
			}
		}
	}

	// Create the max text length for send to translate
	private static String createMaxTextLength() {

		if(ALL_REVIEWS.isEmpty()) {
			return "";
		}

		Boolean enoughTesting = false;
		// Take the last text from the all reviews list
		String text = getLast();
		removeLast();

		// Try to add the most long strings
		while (!ALL_REVIEWS.isEmpty() && text.length() < MAX_LENGTH && !enoughTesting) {

			String tempText = text.concat("\0").concat(getLast());
			// If it too long
			if (tempText.length() > MAX_LENGTH) {
				enoughTesting = true;
			} else { 
				text = tempText;
				removeLast();
			}
		}

		enoughTesting = false;

		// Try to add the most short strings
		while (!ALL_REVIEWS.isEmpty() && text.length() < MAX_LENGTH && !enoughTesting) {

			String tempText = text.concat("\0").concat(getFirst());
			// If it too long
			if (tempText.length() > MAX_LENGTH) {
				enoughTesting = true;
			} else { 
				text = tempText;
				removeFirst();
			}
		}

		return text;
	}

	public static void translate() {
		
		StringBuffer result = new StringBuffer();
		String text = createMaxTextLength();
		
		try {

			String url = "https://api.google.com/translate";
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("input_lang", INPUT_LANG));
			params.add(new BasicNameValuePair("output_lang", OUTPUT_LANG));
			params.add(new BasicNameValuePair("text", text));
			httpPost.setEntity(new UrlEncodedFormEntity(params));
		
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client. execute(httpPost);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		TRANSLATED_REVIEWS.addAll(Arrays.asList(result.toString().split("\0")));
	}

	private static ArrayList<String> cutText(String text) {

		ArrayList<String> strings = new ArrayList<String>();
		
		// Cut the long string to strings with a maximum length of MAX_LENGTH
		while (text.length() > MAX_LENGTH) {
			strings.add(text.substring(0, MAX_LENGTH));
			text = text.substring(MAX_LENGTH);
		}
		
		if (!text.isEmpty()) {
			strings.add(text);
		}
		
		return strings;
	}

	private static void addText(String text) {

		ALL_REVIEWS.add(text);
	}

	private static void sortReviewsByLength() {

		ALL_REVIEWS.sort(new CompStrings());
	}
	
	private static String getFirst() {
		
		return ALL_REVIEWS.get(0);
	}

	private static void removeFirst() {
		
		ALL_REVIEWS.remove(0);
	}
	
	private static String getLast() {
		
		return ALL_REVIEWS.get(ALL_REVIEWS.size() - 1);
	}

	private static void removeLast() {
		
		ALL_REVIEWS.remove(ALL_REVIEWS.size() - 1);
	}
}

class MultiHttpCalls implements Runnable {
	
	Thread httpPostThread;
	
	public MultiHttpCalls() {
		
		httpPostThread = new Thread(this, "Http Post Thread");
		httpPostThread.start();
	}
	
	public void run() {
		
		for (int i = 0 ; i < 100 /*&& stillhascalls*/ ; ++i) {
			TranslateReviews.translate();
		}
	}
}

class CompStrings implements Comparator<String> {

	public int compare(String x1, String x2) {

		return Integer.compare(x1.length(), x2.length());
	}
}