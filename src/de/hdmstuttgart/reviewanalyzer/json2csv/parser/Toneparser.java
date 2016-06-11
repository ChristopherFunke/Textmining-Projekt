package de.hdmstuttgart.reviewanalyzer.json2csv.parser;

import com.ibm.watson.developer_cloud.tone_analyzer.v3_beta.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3_beta.model.ToneAnalysis;
import de.hdmstuttgart.reviewanalyzer.Review;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Toneparser {

    private static final String FILENAME = "config";

    private static ResourceBundle myBundle =
            ResourceBundle.getBundle(FILENAME);
    boolean postiveReview = false;
    String headers = "";
    ArrayList<Review> allReviews = new ArrayList<>();
    private int toneCounter = 0;

    public int getToneCounter() {
        return toneCounter;
    }

    public void setToneCounter(int toneCounter) {
        this.toneCounter = toneCounter;
    }

    public boolean isPostiveReview() {
        return postiveReview;
    }

    public void setPostiveReview(boolean postiveReview) {
        this.postiveReview = postiveReview;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public int getHeadersCount() {
        return this.getHeaders().split(",").length;
    }

    public ToneAnalyzer initToneAnalysis() {

        String username = myBundle.getString("username");
        String password = myBundle.getString("password");

        ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_02_11);
        service.setUsernameAndPassword(username, password);
        service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

        return service;
    }

    public String parseAll(ArrayList<Review> reviewArrayList) throws Exception {
        StringBuilder stb = new StringBuilder();
        String jsonTone = "";
        int headersCount = getHeadersCount();

        stb.append(this.getHeaders());

        for (Review r : reviewArrayList) {
            ToneAnalysis tone = this.initToneAnalysis().getTone(r.getText()).execute();

            jsonTone = tone.getDocumentTone().toString();
            //System.out.println(r.getId() + " " + r.isPositive() +" " + r.getName());
            stb.append(r.getName() + ", ");
            stb.append(this.parseOneReview(jsonTone, r.isPositive()));

        }
        if (this.getToneCounter() != headersCount)
            throw new Exception("Header und toneanzahl ungleich");
        return stb.toString();
    }

    public String parseOneReview(String reviewJson, boolean isPositive) {
        String target = "";
        if (isPositive)
            target = "pos, ";
        else
            target = "neg, ";


        JSONObject json = new JSONObject(reviewJson);
        int jsonLenght = json.length();

        JSONObject arraynodeOfJson;

        StringBuilder stringbuilderCSV = new StringBuilder();

        JSONArray toneCategories = json.getJSONArray("tone_categories");
        int toneCategorieLength = toneCategories.length();
        String[][] Tones = new String[toneCategorieLength][];
        int toneCounter = 2;

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

                if (toneCounter != getHeadersCount())
                    stringbuilderCSV.append(", ");
                else
                    stringbuilderCSV.append("\r\n");
            }

        }
        this.setToneCounter(toneCounter);
        return stringbuilderCSV.toString();
    }

//	public String parseJsonToString (String jsonString) {
//		JSONArray arrayOfJson = new JSONArray(jsonString);
//		int jsonLenght = arrayOfJson.length();
//
//		JSONObject arraynodeOfJson;
//
//		StringBuilder stringbuilderCSV = new StringBuilder();
//
//		stringbuilderCSV.append(headers);
//
//		for (int l = 0; l < jsonLenght; l++){
//
//			arraynodeOfJson = arrayOfJson.getJSONObject(l);
//			JSONArray toneCategories = arraynodeOfJson.getJSONArray("tone_categories");
//			int toneCategorieLength = toneCategories.length();
//			String[][] Tones = new String[toneCategorieLength][];
//			int toneCounter = 1;
//
//			//stringbuilderCSV.append(target);
//
//			for (int i = 0; i < toneCategorieLength; i++) {
//
//				arraynodeOfJson = toneCategories.getJSONObject(i);
//				JSONArray jsonarrayTone = new JSONArray(arraynodeOfJson.getJSONArray("tones").toString());
//				int tonelength = jsonarrayTone.length();
//				Tones[i] = new String[tonelength];
//
//				for (int j = 0; j < tonelength; j++) {
//
//					toneCounter++;
//					arraynodeOfJson = jsonarrayTone.getJSONObject(j);
//					Tones[i][j] = String.valueOf(arraynodeOfJson.getDouble("score"));
//					stringbuilderCSV.append(Tones[i][j]);
//
//					if (toneCounter != 14)
//						stringbuilderCSV.append(", ");
//					else
//						stringbuilderCSV.append("\r\n");
//				}
//
//			}
//		}
//		return stringbuilderCSV.toString();
//	}


    public String readFile(String filename) throws IOException {


        String result = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();

            }

            result = sb.toString();
        }
        return result;
    }

    public ArrayList<Review> readFiles(String directory) throws IOException {

        File dir = new File(directory);


        for (File f : dir.listFiles()) {

            if (f.isFile() && f.getName().toLowerCase().endsWith(".txt")) {
                String review = readFile(f.getAbsolutePath());
                Review r = new Review(postiveReview, review, f.getName());
                //System.out.println(r.getText() + " "+ r.isPositive());
                allReviews.add(r);
            } else if (f.isDirectory()) {
                if (f.getName().equals("pos")) {
                    postiveReview = true;
                    readFiles(f.getAbsolutePath());
                } else if (f.getName().equals("neg")) {
                    postiveReview = false;
                    readFiles(f.getAbsolutePath());
                }

            }

        }
        return allReviews;
    }
}
