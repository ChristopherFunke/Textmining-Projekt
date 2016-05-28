package start_Package;




import java.io.IOException;
import java.util.ArrayList;

import com.ibm.watson.developer_cloud.tone_analyzer.v3_beta.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3_beta.model.ToneAnalysis;

import parser.MovieCriticsParser;


public class Start {
	
	public static void main(String[] args) throws IOException {
		
		MovieCriticsParser m = new MovieCriticsParser();
		String text = m.readFile();

		
		ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_02_11);
		service.setUsernameAndPassword("6370fa92-72ff-4ca0-a300-8fef3d62efb0", "AXCjTnW7vESl");
		service.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");
	

		
		ToneAnalysis tone = service.getTone(text).execute();
		
		
		System.out.println(tone);
		

		
		
	}

}
