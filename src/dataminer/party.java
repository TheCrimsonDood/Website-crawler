package src.dataminer;

import java.io.IOException;

public class Party {
    String name; // Name der Partei
    int articleCount; // Anzahl der Artikel

    private static String pfad = "src/data/Piratenpartei/EU-Abgeordnete Julia Reda im Interview Lieber keine Reform als diese.txt";

    public static void main(String[] args) throws IOException {
        Article article = new Article(pfad);
        article.getArticle();
        article.deleteHTMLFromArticle();
        System.out.println(article.article);
    }
}