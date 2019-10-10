package src.dataminer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class article {

    // --Klassen Variablen--
    int wordCount; // Gesammte Anzahl der Wörter
    HashMap<String, Integer> exactWordCount; // Jedes Wort wird als Key gespeichert, wohingegen die Anzahl als
                                             // dazugehöiges Value fungiert
    BufferedReader br;
    FileReader fr;
    String pfad;

    // --Konstruktor--
    article(String pfad) throws FileNotFoundException {
        this.pfad = pfad;
        readFile();
        bufferFile();
    }

    // --Methoden--
    protected void getDataOutOfArticle() throws IOException {
        //Analysiert den Artikel nach unterschiedlichen Mußtern
        String line;
        while((line = this.br.readLine()) != null){
            getWordsOutofString(line);
            countWordsInString(line);
        }
        bufferFile();
    }

    protected void getWordsOutofString(String line){
        //Zählt alle Wörter im String und dokumentiert die Anzahl

        String wordsInLine[] = line.split("\\s+"); //Trennt die line anhand der Leerzeichen auf 
        for (String word : wordsInLine) {
            word = word.replaceAll("[^\\w]", ""); //Ersetzt alle Satzzeichen mit nichts
            if(exactWordCount.containsKey(word)){ //Überprüft ob das Wort schonmal eingetragen wurde
                this.exactWordCount.put(word, exactWordCount.get(word)+1); //Erhöht die Anzahl um 1
            }else{
                this.exactWordCount.put(word, 1); //Fügt das Wort zur Liste hinzu, falls es noch nicht aufgetreten ist
            }
        }
    }
    protected void countWordsInString(String line){
        String wordsInLine[] = line.split("\\s+"); //Trennt die Line anhand der Leerzeichen auf
        this.wordCount = this.wordCount + wordsInLine.length; //fügt die Nummer der wörter zur gesammtzahl hinzu
    }



    private void readFile() throws FileNotFoundException {
        // ließt die lokale Datei ein
        this.fr = new FileReader(this.pfad);
        return;
    }
    private void bufferFile(){
        this.br = new BufferedReader(fr);
    }
}