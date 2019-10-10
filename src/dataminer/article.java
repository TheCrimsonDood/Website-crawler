package src.dataminer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class Article {

    //Eigentliche Artikel muss noch definiert werden. Bisher arbeiten alle Funtkionen mit der gesamten HTML datei

    // --Klassen Variablen--
    int wordCount; // Gesammte Anzahl der Wörter
    HashMap<String, Integer> exactWordCount; // Jedes Wort wird als Key gespeichert, wohingegen die Anzahl als
                                             // dazugehöiges Value fungiert
    Date date; // Datum des Artikels
    BufferedReader br;
    FileReader fr;
    String pfad;

    // --Konstruktor--
    Article(String pfad) throws IOException {
        this.pfad = pfad;
        readFile();
        bufferFile();
        getDataOutOfArticle();
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

    private void countWordsInString(String line) {
        String wordsInLine[] = line.split("\\s+"); // Trennt die Line anhand der Leerzeichen auf
        this.wordCount = this.wordCount + wordsInLine.length; // fügt die Nummer der wörter zur gesammtzahl hinzu
    }

    protected void getDateOutOfArticle(String line){
        if (line.contains("<meta name=\"date\" content=")){
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