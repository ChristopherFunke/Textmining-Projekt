package de.hdmstuttgart.reviewanalyzer.json2csv.parser;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.Tone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

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

    private static final ResourceBundle myBundle =
            ResourceBundle.getBundle(FILENAME);

    private final ArrayList<Review> allReviews = new ArrayList<>();

    private boolean postiveReview = false;

    private String headers = "";

    private int toneCounter = 0;

    private int getToneCounter() {
        return toneCounter;
    }

    private void setToneCounter(int toneCounter) {
        this.toneCounter = toneCounter;
    }

    public boolean isPostiveReview() {
        return postiveReview;
    }

    public void setPostiveReview(boolean postiveReview) {
        this.postiveReview = postiveReview;
    }

    private String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    private int getHeadersCount() {
        return this.getHeaders().split(",").length;
    }

    private ToneAnalyzer initToneAnalysis() {

        String username = myBundle.getString("username");
        String password = myBundle.getString("password");

        ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
        service.setUsernameAndPassword(username, password);
        service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

        return service;
    }

    /**
     * Parse all Reviews to a CSV-string.
     *
     * @param reviewArrayList the review array list
     * @return CSVstring
     * @throws Exception Header != Tones
     */
    public String parseAll(ArrayList<Review> reviewArrayList) throws Exception {
        StringBuilder stb = new StringBuilder();
        String jsonTone;
        //hole die spaltenanzahl
        int headersCount = getHeadersCount();

        // füge die spaltennamen in die oberste zeile hinzu
        stb.append(this.getHeaders());

        for (Review r : reviewArrayList) {
            //hole den tone für ein review
            ToneAnalysis tone = this.initToneAnalysis().getTone(r.getText(), null).execute();
            jsonTone = tone.getDocumentTone().toString();

            //füge den Namen der review datei in die zeile hinzu
            stb.append(r.getName()).append(", ");

            //füge die tonescores für ein review in die zeile ein
            stb.append(this.parseOneReview(jsonTone, r.isPositive()));

        }
        // spaltenanzahl und anzahl der tones darf nicht != sein
        if (this.getToneCounter() != headersCount)
            throw new Exception("Header und toneanzahl ungleich");

        return stb.toString();
    }

    private String parseOneReview(String reviewJson, boolean isPositive) {
        String target;
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

        //füge in die zeile hinzu ob das review "pos" oder "neg" ist
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

                // wenn es noch nicht der letzte score eines tones ist, dann trenne den score mit einem Komma
                // wenn der letzte dann zeilenumbruch
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


    private String readFile(String filename) throws IOException {


        String result;

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

                // erstelle ein review objekt
                Review r = new Review(postiveReview, review, f.getName());
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
