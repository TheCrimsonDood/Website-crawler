package src.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import javax.swing.filechooser.FileSystemView;

public class main {

    static Boolean isDateExceeded;
    static Date earliestDate;

    public static void main(String[] args0) {

        // --- Variablen ---
        HashSet<String> wordList = links.getInitialWords(); // Ruft die Liste mit den Suchbegriffen auf
        String pathString = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Website-crawler\\data"; // Pfad zum anlegen der Ordner
        int page = 1; // Startseite zum Suchen, beschreibt die Seite der SpiegelOnlineSuche von der die Artikel gesammelt werden
        isDateExceeded = false; //Variable, um zu bestimmen ob der Artikel älter ist als das angegebene Datum. Dadurch sucht das Programm keine weiteren Artikel
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Beschreibt das Datum nachdem die Artikel verfasst sein müssen.
        try {
            earliestDate = sdf.parse("01-01-2019"); // Beschreibt das Datum für die ältesten Artikel. Ältere Atikel als von diesem Datum beschrieben werden nicht berücksichtigt.
        } catch (ParseException e) {
            e.printStackTrace();
        }


        for (String word : wordList) {

            // --- Temporäre Variablen ---
            HashSet<URL> linkList = new HashSet<URL>();
            int count = 1; 
            isDateExceeded = false;
            page = 1;

            // --- Funktionalität ---
            // Erstellt für jedes Suchwort einen Ordner in dem in pathString angegebe Verzeichnis
            String tempPathString = pathString + "\\" + word;
            utilitys.createDirectory(tempPathString);

            do {

                // --- Temporäre Variablen ---
                HashSet<String> stringLinks = null;
                 URL link = links.transformWordToSearchURL(word, page); // Transformiert das Suchwort in ein SuchLink
                // try{
                    BufferedReader reader = crawler.createBR(link);// Erstellt einen Stream und lädt den gesamten Sourcecode in den BufferedReader
                // }catch{                }
                stringLinks = crawler.getLinksOnPage(reader);// Liest alle Artikel Links aus dem Suchergebnis
               
                // --- Funktionalität
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Transformiert alle Eintragungen aus der Artikelsuche in vollwertige Artikellinks und fügt sie einere Eineindeutigen Liste hinzu
                for (String stringLink : stringLinks) {
                    URL urlLink = links.transformWordToArticleURL(stringLink);
                    linkList.add(urlLink);
                }

                page++; //geht zur nächsten Seite der Suche auf Spiegel Online weiter
            } while (main.isDateExceeded ==  false);


            for (URL tempLink : linkList) {
                utilitys.saveToFile(tempLink, tempPathString); //Speichert den Artikel lokal in dem angegebenen Pfad
                System.out.println(word + " running. count = " + count); //Consolenausgabe mit Suchbegriff und der wievielte Artikel gerade gespeichert wurde
                count++;
            }
        }
    }
}