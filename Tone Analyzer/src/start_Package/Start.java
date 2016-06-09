package start_Package;


import com.ibm.watson.developer_cloud.tone_analyzer.v3_beta.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3_beta.model.ToneAnalysis;
import com.jsontocsv.parser.JsonFlattener;
import com.jsontocsv.writer.CSVWriter;
import parser.MovieCriticsParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Start {

	public static void main(String[] args) throws Exception {

		MovieCriticsParser m = new MovieCriticsParser();
		String text = m.readFile();


		ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_02_11);
		service.setUsernameAndPassword("","");
		service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");



		ToneAnalysis tone = (ToneAnalysis) service.getTone(text).execute();






		JsonFlattener parser = new JsonFlattener();
		CSVWriter writer = new CSVWriter();

		List<Map<String, String>> flatJson = parser.parseJson(String.valueOf(tone.getDocumentTone()));
		writer.writeAsCSV(flatJson, "sample.csv");
	}


	}


