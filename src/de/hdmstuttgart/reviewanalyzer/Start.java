package de.hdmstuttgart.reviewanalyzer;

import com.google.common.base.Stopwatch;
import de.hdmstuttgart.reviewanalyzer.json2csv.parser.Toneparser;
import de.hdmstuttgart.reviewanalyzer.json2csv.writer.Csvwriter;

import java.util.ArrayList;
import java.util.ResourceBundle;


class Start {
    //Stopwatch wird gestartet, um die Dauer des Programms zu messen
    private static final Stopwatch timer = Stopwatch.createStarted();

    // Dateiname der Properties-Datei
    private static final String FILENAME = "config";

    //Laden der Properties-Datei in den RessourceBundle
    private static final ResourceBundle myBundle =
            ResourceBundle.getBundle(FILENAME);

    public static void main(String[] args) throws Exception {

        Toneparser tParser = new Toneparser();

        //Einlesen der Reviews aus den txt Dateien in einen Review Array
        ArrayList<Review> reviewArrayList = tParser.readFiles(myBundle.getString("filepathToReviews"));

        // Setzen der Spaltennamen für die csv (Dei Reihenfolge ist wichtig)
        String headers = "Name, Target, Anger, Disgust, Fear, Joy, Sadness, Analytical, Confident, Tentative, Openness, Conscientiousness" +
                ", Extraversion, Agreeableness, Emotional Range\r\n";

        tParser.setHeaders(headers);

        //hollt sich für alle reviews die tone von ibm und wandelt sie in ein cvs format um
        String allParsedJsons = tParser.parseAll(reviewArrayList);

        System.out.println(allParsedJsons);

		Csvwriter writer = new Csvwriter();
        //speichert den csv-string in eine CSV-Datei
        writer.writeToFile(allParsedJsons, "reviews.csv");


        System.out.println("das Programm brauchte: ~" + timer.stop() + " und hat "
                + Review.getCounter() + " Reviews bearbeitet");

	}

}


