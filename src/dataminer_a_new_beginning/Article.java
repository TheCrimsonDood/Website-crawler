package src.dataminer_a_new_beginning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

public class Article {

    // Eigentliche Artikel muss noch definiert werden. Bisher arbeiten alle
    // Funtkionen mit der gesamten HTML datei

    // --Klassen Variablen--
    int wordCount; // Gesammte Anzahl der Wörter
    HashMap<String, Integer> exactWordCount = new HashMap<String, Integer>(); // Jedes Wort wird als Key gespeichert,
                                                                              // wohingegen die Anzahl als
                                                                              // dazugehöiges Value fungiert
    Calendar date = Calendar.getInstance(); // Datum des Artikels
    String author;// Autor des Artikels
    BufferedReader br;
    FileReader fr;
    String path; // Pfad des Artikels
    String article = null; // Der lesbare Artikel als Plain Text
    String[] articleInWords; // der gesamte Artikel als Aray in Wörter zerlegt
    String[] keywords; // Die Keywords/Tags, mit denen der Artikel von SPIEGEL Online hochgeladen wurde
    String category; // Die Kategorie (Netzpolitik/Wirtschaft) unter der der Artikel veröffentlicht
                     // wurde
    Boolean hasComments;

    // --Konstruktor--
    Article(String path) throws IOException, ParseException {
        this.path = path;
        this.date.setLenient(false);
        readFile();
        bufferFile();
        getDateOutOfArticle();
        doCommentsExist();
        getAuthor();
        getKeywords();
        getCategory();
        getArticle();
        deleteHTMLFromArticle();
        getDataOutOfArticle();
    }

    // --Methoden--

    private void getAuthor() throws IOException {
        // Ließt den Autor des Artikels aus

        // ---Variablen---
        int startIndex;
        int endIndex;
        String line;

        // ---Funktionen---
        while ((line = br.readLine()) != null) {
            if (line.contains("rel=\"author\">")) {// Überprüft, ob die Struktur für das Beschreiben des Autors in der
                                                   // Zeile auftaucht
                startIndex = line.indexOf("rel=\"author\">") + 13;
                endIndex = line.indexOf("</a>");
                this.author = line.substring(startIndex, endIndex);
                break;
            } else if (line.contains("<p><i>")) {

                startIndex = line.indexOf("<p><i>") + 6;
                endIndex = line.indexOf("</i></p>");
                this.author = line.substring(startIndex, endIndex);
                break;

            }
        }
        // Sollte der Autor nicht bekannt sein, wird er mit unbeknnt gesetzt
        if (this.author == null) {
            this.author = "Unbekannt";
        }
        readFile();
        bufferFile();
    }

    private void getKeywords() throws IOException {
        // Ließt die Tags des Artikels aus

        // ---Variablen---
        String line;
        int startIndex;
        int endIndex;
        String keywordString;

        // ---Funktionen---
        while ((line = br.readLine()) != null) {
            if (line.contains("<meta name=\"news_keywords\" content=\"")) {// Überprüft, ob die Struktur für das
                                                                           // Beschreiben der Kategorie in der Zeile
                                                                           // auftaucht
                startIndex = line.indexOf("<meta name=\"news_keywords\" content=\"") + 36;
                endIndex = line.indexOf("\" />");
                keywordString = line.substring(startIndex, endIndex);
                this.keywords = keywordString.split(", ");
                break;
            }
        }
        readFile();
        bufferFile();
    }

    private void doCommentsExist() throws IOException {
        // checks, if a article has comment function enabled
        // --- Variablen ---
        String line;
        this.hasComments = false;
        // --- Funktionalität ---
        while ((line = br.readLine()) != null) {
            if (line.contains("id=\"js-article-comments-box-pager\"")) {
                this.hasComments = true;
                break;
            }
        }
        readFile();
        bufferFile();

    }

    private void getDataOutOfArticle() throws IOException {
        // Analysiert den Artikel nach unterschiedlichen Mußtern
        transformArticleInWords();
        countWordsInArticle();
        getWordsOutofArticle();
    }

    private void getCategory() throws IOException {
        // Ließt die Kategorie, in der der Artikel veröffentlicht wurde

        // ---Variablen---
        String line;
        int startIndex;
        int endIndex;

        // ---Funktionalität---
        while ((line = br.readLine()) != null) {
            if (line.contains("\"Rubrik:")) {
                startIndex = line.indexOf("\"Rubrik:") + 9;
                endIndex = line.indexOf("\"]");
                this.category = line.substring(startIndex, endIndex);
                break;
            }
        }
        readFile();
        bufferFile();
    }

    private void getWordsOutofArticle() {
        // Zählt alle Wörter im String und dokumentiert die Anzahl
        for (String word : this.articleInWords) {
            word = word.toLowerCase();
            if (exactWordCount.containsKey(word)) { // Überprüft ob das Wort schonmal eingetragen wurde
                this.exactWordCount.put(word, exactWordCount.get(word) + 1); // Erhöht die Anzahl um 1
            } else {
                this.exactWordCount.put(word, 1); // Fügt das Wort zur Liste hinzu, falls es noch nicht aufgetreten ist
            }
        }
    }

    protected void countWordsInArticle() {
        String[] words = this.article.split(" ");
        this.wordCount = words.length;
    }

    private void transformArticleInWords() {
        String article = this.article;
        article = article.replaceAll("[^a-zA-Z 0-9äöüß]", " ");// Ersetzt alle Satzzeichen mit Leerstellen

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
                            if (endIndex > startIndex) {
                                if (this.article != null) {
                                    this.article += line.substring(startIndex, endIndex) + "\n";
                                } else {
                                    this.article = line.substring(startIndex, endIndex) + "\n";
                                }
                            }
                        } else {// sollte der endtag nicht folgen wird die gesamte Zeile rausgeschrieben
                            endIndex = line.length();
                            if (this.article != null) {
                                this.article += line.substring(startIndex, endIndex) + "";
                            } else {
                                this.article = line.substring(startIndex, endIndex) + "\n";
                            }
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
            this.article = this.article.replaceAll("<br/>", "");
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
            if (line.contains("<meta name=\"date\" content=")) {// überprüft, ob die Zeile den angegebenen String
                                                                // beinhält, um das Datum zu finden
                int startIndex = line.indexOf("<meta name=\"date\" content=");// Setzt den startIndex im String
                int endIndex = line.indexOf("+");// Setzt den endIndex im String
                String dateTime = line.substring(startIndex + 27, endIndex - 1); // Erstellt einen Substring aus line
                                                                                 // indem sich das Datum befindet

                String[] date = dateTime.split("T");// Splitted Datum und Zeit anhand des T
                String[] dateSplit = date[0].split("-");
                String[] timeSplit = date[1].split(":");
                this.date.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1])-1, Integer.parseInt(dateSplit[2]), Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1]), Integer.parseInt(timeSplit[2]));// Konvertiert die beiden Strings in ein Datum
                break;
            }

        }
        readFile();
        bufferFile();

    }

    private void readFile() throws FileNotFoundException {
        // ließt die lokale Datei ein
        this.fr = new FileReader(this.path);
        return;
    }

    private void bufferFile() {
        this.br = new BufferedReader(fr);
    }
}