package de.hdmstuttgart.reviewanalyzer;

/**
 * Created by danielvolz on 10.06.16.
 */
public class Review {
    private static int counter = 0;
    private String text = "";
    private boolean positive= false;
    private int id = 0;
    private String name ="";

    public Review(boolean positive, String text, String name) {
        this.name = name;
        this.positive = positive;
        this.text = text;
        this.id = counter++;
    }

    public static int getCounter() {
        return counter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public int getId() {
        return id;
    }

}
