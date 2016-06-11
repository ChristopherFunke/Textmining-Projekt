package de.hdmstuttgart.reviewanalyzer.json2csv.writer;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Csvwriter {


    public void writeToFile(String output, String fileName) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
    }

    private void close(BufferedWriter writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
