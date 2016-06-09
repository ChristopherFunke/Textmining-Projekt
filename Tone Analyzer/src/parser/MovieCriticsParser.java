package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MovieCriticsParser {


	//	FileReader fr = new FileReader("C:\\Users\\Christopher\\Desktop\\TestOrdner\\ShakespearePlaysPlus\\TXT");
	public String readFile() throws IOException {
		String result = "";

		try(BufferedReader br = new BufferedReader(new FileReader("/Users/danielvolz/git/Textmining-Projekt/Tone Analyzer/Batman v Superman/Critics/The New Yorker -- Bad.txt"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			result = everything;
		}
		return result ;
	}
}
