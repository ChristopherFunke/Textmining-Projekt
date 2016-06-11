package de.hdmstuttgart.reviewanalyzer.json2csv.parser;

import de.hdmstuttgart.reviewanalyzer.Review;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class Toneparser {

	boolean postiveReview = false;

	ArrayList<Review> allReviews = new ArrayList<>();

	public boolean isPostiveReview() {
		return postiveReview;
	}

	public void setPostiveReview(boolean postiveReview) {
		this.postiveReview = postiveReview;
	}

	String headers = "";

	// String target = "";

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}



	public String parseOneReview (String reviewJson, boolean isPositive) {
		String target = "";
		if (isPositive) {
			target = "pos, ";
		} else {
			target = "neg, ";
		}
		JSONObject json = new JSONObject(reviewJson);
		int jsonLenght = json.length();

		JSONObject arraynodeOfJson;

		StringBuilder stringbuilderCSV = new StringBuilder();

		//stringbuilderCSV.append(headers);
		JSONArray toneCategories = json.getJSONArray("tone_categories");
		int toneCategorieLength = toneCategories.length();
		String[][] Tones = new String[toneCategorieLength][];
		int toneCounter = 1;

		stringbuilderCSV.append(target);

		for (int i = 0; i < toneCategorieLength; i++) {

			arraynodeOfJson = toneCategories.getJSONObject(i);
			JSONArray jsonarrayTone = new JSONArray(arraynodeOfJson.getJSONArray("tones").toString());
			int tonelength = jsonarrayTone.length();
			Tones[i] = new String[tonelength];

			for (int j = 0; j < tonelength; j++) {

				toneCounter++;
				arraynodeOfJson = jsonarrayTone.getJSONObject(j);
				Tones[i][j] = String.valueOf(arraynodeOfJson.getDouble("score"));
				stringbuilderCSV.append(Tones[i][j]);

				if (toneCounter != 14)
					stringbuilderCSV.append(", ");
				else
					stringbuilderCSV.append("\r\n");
			}

		}

		return stringbuilderCSV.toString();
	}

	public String parseJsonToString (String jsonString) {
		JSONArray arrayOfJson = new JSONArray(jsonString);
		int jsonLenght = arrayOfJson.length();

		JSONObject arraynodeOfJson;

		StringBuilder stringbuilderCSV = new StringBuilder();

		stringbuilderCSV.append(headers);

		for (int l = 0; l < jsonLenght; l++){

			arraynodeOfJson = arrayOfJson.getJSONObject(l);
			JSONArray toneCategories = arraynodeOfJson.getJSONArray("tone_categories");
			int toneCategorieLength = toneCategories.length();
			String[][] Tones = new String[toneCategorieLength][];
			int toneCounter = 1;

			//stringbuilderCSV.append(target);

			for (int i = 0; i < toneCategorieLength; i++) {

				arraynodeOfJson = toneCategories.getJSONObject(i);
				JSONArray jsonarrayTone = new JSONArray(arraynodeOfJson.getJSONArray("tones").toString());
				int tonelength = jsonarrayTone.length();
				Tones[i] = new String[tonelength];

				for (int j = 0; j < tonelength; j++) {

					toneCounter++;
					arraynodeOfJson = jsonarrayTone.getJSONObject(j);
					Tones[i][j] = String.valueOf(arraynodeOfJson.getDouble("score"));
					stringbuilderCSV.append(Tones[i][j]);

					if (toneCounter != 14)
						stringbuilderCSV.append(", ");
					else
						stringbuilderCSV.append("\r\n");
				}

			}
		}
		return stringbuilderCSV.toString();
	}



	public String readFile(String filename) throws IOException {


		String result = "";

		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();

			}

			result = sb.toString();
		}
		return result ;
	}

	public ArrayList<Review> readFiles(String directory) throws IOException {

		File dir = new File(directory);


		for (File f: dir.listFiles()) {

			if (f.isFile() && f.getName().toLowerCase().endsWith(".txt")) {
				String review = readFile(f.getAbsolutePath());
				Review r = new Review(postiveReview, review, f.getName());
				//System.out.println(r.getText() + " "+ r.isPositive());
				allReviews.add(r);
			} else if (f.isDirectory()) {
				if (f.getName().equals("pos")) {
					//System.out.println(f.getName());
					postiveReview = true;
					readFiles(f.getAbsolutePath());
				} else if (f.getName().equals("neg")) {
					//  System.out.println(f.getName());
					postiveReview = false;
					readFiles(f.getAbsolutePath());
				}


			}

		}
		return allReviews;
	}
}
