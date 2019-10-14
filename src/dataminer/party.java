package src.dataminer;

import java.io.IOException;
import java.text.ParseException;

public class Party {
    String name; // Name der Partei
    int articleCount; // Anzahl der Artikel

    private static String pfad = "src/data/Piratenpartei/EU-Abgeordnete Julia Reda im Interview Lieber keine Reform als diese.txt";

    public static void main(String[] args) throws IOException, ParseException {
        Article article = new Article(pfad);
        System.out.println(article.article);
    }
}