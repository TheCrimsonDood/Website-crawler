package src.dataminer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class Article {

    // Eigentliche Artikel muss noch definiert werden. Bisher arbeiten alle
    // Funtkionen mit der gesamten HTML datei

    // --Klassen Variablen--
    int wordCount; // Gesammte Anzahl der Wörter
    HashMap<String, Integer> exactWordCount; // Jedes Wort wird als Key gespeichert, wohingegen die Anzahl als
                                             // dazugehöiges Value fungiert
    Date date; // Datum des Artikels
    BufferedReader br;
    FileReader fr;
    String pfad;
    String article = null;

    // --Konstruktor--
    Article(String pfad) throws IOException {
        this.pfad = pfad;
        readFile();
        bufferFile();
        // getDataOutOfArticle();
    }

    // --Methoden--
    protected void getDataOutOfArticle() throws IOException {
        // Analysiert den Artikel nach unterschiedlichen Mußtern
        String line;
        while ((line = this.br.readLine()) != null) {
            getWordsOutofString(line);
            countWordsInString(line);
        }
        bufferFile();
    }

    private void getWordsOutofString(String line) {
        // Zählt alle Wörter im String und dokumentiert die Anzahl

        String wordsInLine[] = line.split("\\s+"); // Trennt die line anhand der Leerzeichen auf
        for (String word : wordsInLine) {
            word = word.replaceAll("[^\\w]", ""); // Ersetzt alle Satzzeichen mit nichts
            if (exactWordCount.containsKey(word)) { // Überprüft ob das Wort schonmal eingetragen wurde
                this.exactWordCount.put(word, exactWordCount.get(word) + 1); // Erhöht die Anzahl um 1
            } else {
                this.exactWordCount.put(word, 1); // Fügt das Wort zur Liste hinzu, falls es noch nicht aufgetreten ist
            }
        }
    }

    protected void countWordsInString(String line) {
        String wordsInLine[] = line.split("\\s+"); // Trennt die Line anhand der Leerzeichen auf
        this.wordCount = this.wordCount + wordsInLine.length; // fügt die Nummer der wörter zur gesammtzahl hinzu
    }

    void getArticle() throws IOException {
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
                            this.article += line.substring(startIndex, endIndex) + "\n";
                        }

                    }
                } else {
                    startIndex = 0;// Wird gesetzt, da ab beginn der Zeile Text aus dem Artikel steht
                    if (line.contains("</p>")) {
                        // Überprüft ob das Ende Element von P alleine in der line steht
                        if (line.indexOf("</p>") > 0) {
                            endIndex = line.indexOf("</p>") - 1;
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

    }

    protected void getDateOutOfArticle(String line) {
        if (line.contains("<meta name=\"date\" content=")) {
            int startIndex = line.indexOf("content=");
            int endIndex = line.indexOf("/");

        }
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