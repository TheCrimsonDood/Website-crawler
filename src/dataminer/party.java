package src.dataminer;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Party {
    String name; // Name der Partei
    int articleCount = 0; // Anzahl der Artikel
    HashMap<String, Integer> categorys = new HashMap<String, Integer>(); // Auflistung der Kategorien in denen die
                                                                         // Artikel veröffentlicht wurden mit Anzahl
    int beforeEU = 0;// Anzahl der Artikel, die vor der Eu-Wahl herausgebracht wurden
    int afterEU = 0; // ANzahl der Artikel, die nach der EU-Wahl herausgebracht wurden
    int whileEU = 0; //Anzahl der Artikel, die während der EU-Wahl veröffentlicht wurden
    HashMap<String, Integer> releaseMonth = new HashMap<String, Integer>(); // Zählt die Anzahl der Artikel pro Monat
    int averageArticleLength;
    HashMap<String, Integer> authorCount = new HashMap<String, Integer>();

    Party(String party,String path) throws IOException, ParseException {
        //---Variablen---
        this.name = party;

        File folder = new File(path + name); // Erstellt den Ordnerpfad anhand des Namens des Objektes
        File[] listOfFiles = folder.listFiles(); // Liest alle Datein im Ordner
        int totalLength=0;//Gesamtlänge aller Artikel


        //---Funktionen---
        for (File file : listOfFiles) { // Schleife, die folgende Aktionen für jede Datei ausführen
            if (file.isFile()) {
                Article article = new Article(folder+ file.getName());//Öffnet einen Artikel und ließt wichtige Daten aus
                this.articleCount++; //Erhöht die Varaible um 1, da sie die Anzahl der Artikel wiedergibt
                totalLength+=article.wordCount;
                getAuthorFromArticle(article.author);//Ließt den Autor aus und speichert Ihn in der Variable authorCount
                getDateFromArticle(article.date);

            }
        }
        this.averageArticleLength = totalLength/this.articleCount;
    }

    private void getDateFromArticle(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date euStart = sdf.parse("2019-05-23 00:00:00");
        Date euEnd = sdf.parse("2019-05-26 23:59:59");

        if(date.after(euEnd)){
            beforeEU++;
        }else if(date.before(euStart)){
            afterEU++;
        }else{
            whileEU++;
        }
        Calendar calendar;
        Switch(date.getMonth()){

        }
        
    }

    private void getAuthorFromArticle(String author){
        //Schreibt den Übergebenen Autor in die Varible
        if(authorCount.containsKey(author)){//Überprüft, ob der Autor schon in die Variable gespeichert wurde
            authorCount.put(author, authorCount.get(author)+1);//Wenn ja, wird die Anzahl um 1 erhöht
        }else{
            authorCount.put(author, 1);//Wenn nicht, wird er mit dem Wert 1 hinzugefügt
        }
    }

    private static String pfad = "src/data/Piratenpartei/EU-Abgeordnete Julia Reda im Interview Lieber keine Reform als diese.txt";
    // private static String pfad = "src/resources/countWordTest.txt";

    public static void main(String[] args) throws IOException, ParseException {
        Article article = new Article(pfad);
        System.out.println(article.article);
    }
}