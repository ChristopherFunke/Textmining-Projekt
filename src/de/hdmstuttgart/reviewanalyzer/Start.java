package de.hdmstuttgart.reviewanalyzer;

import com.ibm.watson.developer_cloud.tone_analyzer.v3_beta.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3_beta.model.ToneAnalysis;
import de.hdmstuttgart.reviewanalyzer.json2csv.parser.Toneparser;
import de.hdmstuttgart.reviewanalyzer.json2csv.writer.Csvwriter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


public class Start {

    /**
     * Properties file name.
     */
    private static final String FILENAME = "config";

	private static ResourceBundle myBundle =
			ResourceBundle.getBundle(FILENAME);

    public static void main(String[] args) throws Exception {
        double startTime = System.currentTimeMillis();

        Toneparser tParser = new Toneparser();

        ArrayList<Review> reviewArrayList = tParser.readFiles(myBundle.getString("filepathToReviews"));


        String headers = "Name, Target, Anger, Disgust, Fear, Joy, Sadness, Analytical, Confident, Tentative, Openness, Conscientiousness" +
                ", Extraversion, Agreeableness, Emotional Range\r\n";

        tParser.setHeaders(headers);

        String allParsedJsons = tParser.parseAll(reviewArrayList);

        System.out.println(allParsedJsons);

		Csvwriter writer = new Csvwriter();

        writer.writeToFile(allParsedJsons, "reviews.csv");

        double endTime = System.currentTimeMillis();
        double totalTime = endTime - startTime;

        System.out.println("das Programm brauchte: ~" + (totalTime / 1000d) / 60d + "min und hat "
                + Review.getCounter() + " Reviews bearbeitet");

	}

}


