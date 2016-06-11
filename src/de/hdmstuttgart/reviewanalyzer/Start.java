package de.hdmstuttgart.reviewanalyzer;

import com.google.common.base.Stopwatch;
import de.hdmstuttgart.reviewanalyzer.json2csv.parser.Toneparser;
import de.hdmstuttgart.reviewanalyzer.json2csv.writer.Csvwriter;

import java.util.ArrayList;
import java.util.ResourceBundle;


class Start {
    private static final Stopwatch timer = Stopwatch.createStarted();
    /**
     * Properties file name.
     */
    private static final String FILENAME = "config";

    private static final ResourceBundle myBundle =
            ResourceBundle.getBundle(FILENAME);

    public static void main(String[] args) throws Exception {

        Toneparser tParser = new Toneparser();

        ArrayList<Review> reviewArrayList = tParser.readFiles(myBundle.getString("filepathToReviews"));


        String headers = "Name, Target, Anger, Disgust, Fear, Joy, Sadness, Analytical, Confident, Tentative, Openness, Conscientiousness" +
                ", Extraversion, Agreeableness, Emotional Range\r\n";

        tParser.setHeaders(headers);

        String allParsedJsons = tParser.parseAll(reviewArrayList);

        System.out.println(allParsedJsons);

		Csvwriter writer = new Csvwriter();

        writer.writeToFile(allParsedJsons, "reviews.csv");


        System.out.println("das Programm brauchte: ~" + timer.stop() + " und hat "
                + Review.getCounter() + " Reviews bearbeitet");

	}

}


