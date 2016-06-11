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

	public static void main(String[] args) throws IOException {

        String json = "{\n" +
                "  \"tone_categories\": [\n" +
                "    {\n" +
                "      \"category_id\": \"emotion_tone\",\n" +
                "      \"category_name\": \"Emotion Tone\",\n" +
                "      \"tones\": [\n" +
                "        {\n" +
                "          \"tone_id\": \"anger\",\n" +
                "          \"tone_name\": \"Anger\",\n" +
                "          \"score\": 0.998713\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"disgust\",\n" +
                "          \"tone_name\": \"Disgust\",\n" +
                "          \"score\": 0.057193\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"fear\",\n" +
                "          \"tone_name\": \"Fear\",\n" +
                "          \"score\": 0.513824\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"joy\",\n" +
                "          \"tone_name\": \"Joy\",\n" +
                "          \"score\": 0.064607\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"sadness\",\n" +
                "          \"tone_name\": \"Sadness\",\n" +
                "          \"score\": 0.054664\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"category_id\": \"writing_tone\",\n" +
                "      \"category_name\": \"Writing Tone\",\n" +
                "      \"tones\": [\n" +
                "        {\n" +
                "          \"tone_id\": \"analytical\",\n" +
                "          \"tone_name\": \"Analytical\",\n" +
                "          \"score\": 0.379\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"confident\",\n" +
                "          \"tone_name\": \"Confident\",\n" +
                "          \"score\": 0.0\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"tentative\",\n" +
                "          \"tone_name\": \"Tentative\",\n" +
                "          \"score\": 0.408\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"category_id\": \"social_tone\",\n" +
                "      \"category_name\": \"Social Tone\",\n" +
                "      \"tones\": [\n" +
                "        {\n" +
                "          \"tone_id\": \"openness_big5\",\n" +
                "          \"tone_name\": \"Openness\",\n" +
                "          \"score\": 0.765\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"conscientiousness_big5\",\n" +
                "          \"tone_name\": \"Conscientiousness\",\n" +
                "          \"score\": 0.501\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"extraversion_big5\",\n" +
                "          \"tone_name\": \"Extraversion\",\n" +
                "          \"score\": 0.563\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"agreeableness_big5\",\n" +
                "          \"tone_name\": \"Agreeableness\",\n" +
                "          \"score\": 0.425\n" +
                "        },\n" +
                "        {\n" +
                "          \"tone_id\": \"neuroticism_big5\",\n" +
                "          \"tone_name\": \"Emotional Range\",\n" +
                "          \"score\": 0.35\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";


		Toneparser m = new Toneparser();

		ArrayList<Review> reviewArrayList = m.readFiles(myBundle.getString("filepathToReviews"));

		String username = myBundle.getString("username");
		String password = myBundle.getString("password");

		ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_02_11);
		service.setUsernameAndPassword(username, password);
		service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");

		String headers ="Target, Anger, Disgust, Fear, Joy, Sadness, Analytical, Confident, Tentative, Openness, Conscientiousness" +
				", Extraversion, Agreeableness, Emotional Range\n\n";

		StringBuilder stb = new StringBuilder();
		stb.append(headers);

		String jsonTone = "";

		for(Review r: reviewArrayList){
			//ToneAnalysis tone = (ToneAnalysis) service.getTone(r.getText()).execute();

			//jsonTone = tone.getDocumentTone().toString();
			System.out.println(r.getId() + " " + r.isPositive() +" " + r.getName());
			//stb.append(tParse.parseOneReview(jsonTone, r.isPositive()));

		}


		System.out.println(stb);


		Csvwriter writer = new Csvwriter();


		//String csvString = tParse.parseJsonToString(buffer.toString());

		//System.out.println(csvString);

		writer.writeToFile(stb.toString(), "reviews.csv");

	}

}


