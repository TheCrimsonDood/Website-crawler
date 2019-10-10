package src.dataminer;

import java.io.IOException;

public class Party {
    String name; // Name der Partei
    int articleCount; // Anzahl der Artikel

    private static String pfad = "src/data/AFD/AfD und der Strache-Skandal Ignorieren, ablenken, aussitzen.txt";

    public static void main(String[] args) throws IOException {
        Article article = new Article(pfad);
    }
}