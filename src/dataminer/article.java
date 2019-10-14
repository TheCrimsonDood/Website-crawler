package src.dataminer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Article {

    // Eigentliche Artikel muss noch definiert werden. Bisher arbeiten alle
    // Funtkionen mit der gesamten HTML datei

    // --Klassen Variablen--
    int wordCount; // Gesammte Anzahl der Wörter
    HashMap<String, Integer> exactWordCount = new HashMap<String, Integer>(); // Jedes Wort wird als Key gespeichert, wohingegen die Anzahl als
                                             // dazugehöiges Value fungiert
    Date date; // Datum des Artikels
    BufferedReader br;
    FileReader fr;
    String pfad;
    String article = null;
    String[] articleInWords;

    // --Konstruktor--
    Article(String pfad) throws IOException, ParseException {
        this.pfad = pfad;
        readFile();
        bufferFile();
        getDateOutOfArticle();
        getArticle();
        deleteHTMLFromArticle();
        getDataOutOfArticle();
    }

    // --Methoden--
    private void getDataOutOfArticle() throws IOException {
        // Analysiert den Artikel nach unterschiedlichen Mußtern
        transformArticleInWords();
        countWordsInArticle();
        getWordsOutofArticle();
        System.out.println("stop");
    }

    private void getWordsOutofArticle() {
        // Zählt alle Wörter im String und dokumentiert die Anzahl

        for (String word : this.articleInWords) {
            if (exactWordCount.containsKey(word)) { // Überprüft ob das Wort schonmal eingetragen wurde
                this.exactWordCount.put(word, exactWordCount.get(word) + 1); // Erhöht die Anzahl um 1
            } else {
                this.exactWordCount.put(word, 1); // Fügt das Wort zur Liste hinzu, falls es noch nicht aufgetreten ist
            }
        }
    }

    protected void countWordsInArticle() {
        wordCount = this.articleInWords.length;
    }

    private void transformArticleInWords() {
        String article = this.article;
         article= article.replaceAll("[^a-zA-Z 0-9äöüß]", " ");// Ersetzt alle Satzzeichen mit nichts

        this.articleInWords = article.split(" ");
    }

    private void getArticle() throws IOException {
        // Extrahiert aus dem HTML Source Code den plain Text Artikel

        // ---Variablen---
        String line;
        int startIndex = 0;
        int endIndex;
        boolean foundArticle = false;
        boolean foundP = false;

        // ---Funktionen---

        while ((line = this.br.readLine()) != null) {
            if (line.contains("<title>")) { // Überprüft ob der Artikeltitel vorhanden ist und speichert ihn in
                                            // article
                startIndex = line.indexOf("<title>");
                endIndex = line.indexOf("</title>");

                this.article = line.substring(startIndex + 7, endIndex - 17) + "\n";
                System.out.println(article);
            }
            if (line.contains("<div class=\"article-function-box clearfix\"")) {
                foundArticle = true;
            }
            if (foundArticle == true) {
                if (foundP == false) {// Wird durchlaufen wenn vorher noch kein P Element gefunden wurde
                    if (line.contains("<p>")) {

                        int pIndex = line.indexOf("<p>") + 2; // position, an der das <p> element aufhört

                        // Überprüft, ob noch mehr inhalt in der zeile ist außer das <p> element
                        if (pIndex >= line.length()) {
                            foundP = true;

                        } else {// Sollte mehr inhalt folgen, wird der startIndex gesetzt
                            startIndex = pIndex + 1;
                            foundP = true;
                        }
                        if (line.contains("</p>")) {// Sollte der ende Tag folgen, wird das rausschreiben finalisiert
                            foundP = false;// wird gesetzt, da der gesamte Text gefunden wurde
                            endIndex = line.indexOf("</p>") - 1;
                            this.article += line.substring(startIndex, endIndex) + "\n";

                        } else {// sollte der endtag nicht folgen wird die gesamte Zeile rausgeschrieben
                            endIndex = line.length();
                            this.article += line.substring(startIndex, endIndex) + "";
                        }

                    }
                } else {
                    startIndex = 0;// Wird gesetzt, da ab beginn der Zeile Text aus dem Artikel steht
                    if (line.contains("</p>")) {
                        // Überprüft ob das Ende Element von P alleine in der line steht
                        if (line.indexOf("</p>") > 0) {
                            endIndex = line.indexOf("</p>");
                        } else {
                            endIndex = 0;
                        }
                        this.article += line.substring(startIndex, endIndex) + "\n";
                        foundP = false; // wird gesetzt, da das P Element abgeschlossen ist
                    } else {
                        this.article += line;// Überträgt die gesamte line in den Artikel, da der gesamte String
                                             // Artikeltext
                                             // enthält
                    }
                }
            }

        }

        br.close();
    }

    private void deleteHTMLFromArticle() {
        // Löscht HTML aus dem Plain Text Artikel

        // ---Variablen---
        // ---Funktionen---
        while (containsArticleHTML() == true) {
            this.article = this.article.replaceAll("<b>", "");
            this.article = this.article.replaceAll("</b>", "");
            this.article = this.article.replaceAll("<a.+?(>)", "");
            this.article = this.article.replaceAll("</a>", "");
            this.article = this.article.replaceAll("<strong>", "");
            this.article = this.article.replaceAll("</strong>", "");
            this.article = this.article.replaceAll("<br />", "");
        }
    }

    private boolean containsArticleHTML() {
        // Überprüft, ob sich noch HTML im Artikel befindet
        Boolean containsHTML = false;

        if (this.article.contains("<b>")) {
            containsHTML = true;
        } else if (this.article.contains("</b>")) {
            containsHTML = true;
        } else if (this.article.contains("<a")) {
            // containsHTML = true;
        } else if (this.article.contains("</a>")) {
            containsHTML = true;
        } else if (this.article.contains("<strong>")) {
            containsHTML = true;
        } else if (this.article.contains("</strong>")) {
            containsHTML = true;
        } else {
            containsHTML = false;
        }
        return containsHTML;
    }

    protected void getDateOutOfArticle() throws IOException, ParseException {
        // Extrahiert das Datum aus dem HTML Source Code
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("<meta name=\"date\" content=")) {//überprüft, ob die Zeile den angegebenen String beinhält, um das Datum zu finden
                int startIndex = line.indexOf("<meta name=\"date\" content=");//Setzt den startIndex im String
                int endIndex = line.indexOf("+");//Setzt den endIndex im String
                String dateTime = line.substring(startIndex+27, endIndex - 1); //Erstellt einen Substring aus line indem sich das Datum befindet

                String[] date = dateTime.split("T");//Splitted Datum und Zeit anhand des T
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//Erstellt ein Datumsformat, um die Strings in ein Datum zu Transformieren
                this.date = sdf.parse(date[0] + " " + date[1]); //Konvertiert die beiden Strings in ein Datum
                break;
            }
            
        }readFile();
        bufferFile();

    }

    private void readFile() throws FileNotFoundException {
        // ließt die lokale Datei ein
        this.fr = new FileReader(this.pfad);
        return;
    }

    private void bufferFile() {
        this.br = new BufferedReader(fr);
    }
}