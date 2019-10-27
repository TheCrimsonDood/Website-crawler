package src.crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;

public class crawler {

    public static HashSet<String> getLinksOnPage(BufferedReader br) {
        // Sucht alle Links zu weiterführenden Artikeln im Sourcecode

        // --- Variablen ---
        String line = null;
        HashSet<String> allLinks = new HashSet<String>(); // HashSet mit jedem gefundenen Link im SourceCode

        // --- Funktionalität ---
        try {
            while ((line = br.readLine()) != null) {

                if (line.contains("<div class=\"search-teaser\">")) { // Struktur, mit der Artikel definiert werden

                    // --- Temporäre Variablen ---
                    String tempSource = br.readLine(); // liest die nächste Zeile, da dort der eigentliche Link
                                                       // liegt
                    String stringWithLink = null;
                    int index = tempSource.indexOf(">"); // Gibt den index des ersten auftauchens von ">" zurück, um
                                                         // das
                                                         // ende des Links zu kennen

                    // --- Funktionalität ---
                    if (index != -1) {

                        // --- Temporäre Variablen ---
                        Date articleDate = null;
                        int dateIndex;

                        /*
                         * Auszug des Originals: <a href=
                         * "/politik/deutschland/sachsen-und-brandenburg-die-ergebnisse-der-kleinparteien-bei-der-landtagswahl-a-1284827.html"
                         * ><strong class="article-title">
                         *//*
                            * substring(13, index -1) schneided alles bis zum ersten / weg und index ab dem
                            * ersten > ab
                            */
                        stringWithLink = tempSource.substring(13, index - 1);

                        // Extrahiert das Datum des Artikels 2 Zeilen tiefer
                        br.readLine();
                        tempSource = br.readLine();
                        dateIndex = tempSource.indexOf("</div>");

                        // Extrahiert das Datum als String von tempSource und übergibt es an
                        // stringToDate um ein richtiges datum daraus zu formen
                        articleDate = utilitys.stringToDate(tempSource.substring(dateIndex - 10, dateIndex));

                        // überprüft, ob der Artikel nach oder an dem angegebenen Datum geschrieben
                        // wurde
                        // wenn nicht, wird die Methode verlassen und mit dem nächsten Suchwort
                        // weitergemacht
                        if (articleDate.compareTo(main.earliestDate) < 0) {
                            main.isDateExceeded = true;
                            return allLinks;
                        }
                        allLinks.add(stringWithLink);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allLinks;

    }

    public static BufferedReader createBR(URL url) {
        // lädt den Source Code von der Übergebenen URL

        // --- Variablen ---
        BufferedReader br = null;
        InputStreamReader isr = null;

        // --- Funktionalität ---
        try {
            // Versucht die Internetseite aufzurufen und den Sourcecode abzufragen
            isr = new InputStreamReader(url.openStream());
            br = new BufferedReader(isr);
        } catch (FileNotFoundException e) {

            try {
                // Falls die seite nicht mehr existiert, wird eine locale Datei ausgelesen mit
                // der Nachricht Error
                FileReader fr = new FileReader("src/resources/Error handler.txt");
                br = new BufferedReader(fr);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            System.out.println("FileNotFoundException");
        } catch (IOException e) {
            System.out.println("Error while opening Site. Go to next");
        }

        return br;

    }

    public static String getHeadline(BufferedReader br) {
        // Sucht aus dem Artikel Quelltext die Überschrift

        // --- Variablen ---
        String line = null;
        String headline = new String();

        // --- Funktionalität ---
        try {
            while ((line = br.readLine()) != null) {
                if (line.contains("<span class=\"headline-intro\">")) {// Struktur, mit der die Headline definiert wird
                    try {
                        // Extrahiert den Intro teil der Headline eines Spiegel Online Artikels
                        int startIndex = line.indexOf(">"); // Gibt den index des ersten auftauchens von ">" zurück, um
                                                            // den
                                                            // anfang der HeadLine zu bestimmen
                        int endIndex = line.indexOf("</span>"); // Gibt den Index des ersten auftauchends von >/span>
                                                                // zurück,
                                                                // um das ende der Headline zu Bestimmen
                        headline = line.substring(startIndex + 1, endIndex);
                        line = br.readLine();

                        //Extrahiert die Headline eines Spiegel Online Artikels
                        startIndex = line.indexOf(">");
                        endIndex = line.indexOf("</span>");

                        headline = headline + " " + line.substring(startIndex + 1, endIndex);
                    } catch (StringIndexOutOfBoundsException e) {
                        headline = "ERROR, Artikelheadline fehlerhaft";
                        System.out.println("StringIndexOutOfBoundsException");
                        e.printStackTrace();
                    }catch(NullPointerException e){
                        headline = "ERROR, Artikelheadline nicht gefunden";
                    }

                    if(headline.contains("Der Tag kompakt") || headline.contains("Die Lage am")){
                        main.genericCount++;
                        headline = headline + " " + main.genericCount;
                    }

                    headline = headline.replace("\\", "");
                    headline = headline.replace("/", "");
                    headline = headline.replace(":", "");
                    headline = headline.replace("*", "");
                    headline = headline.replace("?", "");
                    headline = headline.replace("\"", "");
                    headline = headline.replace("<", "");
                    headline = headline.replace(">", "");
                    headline = headline.replace("|", "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch(NullPointerException e){
            
        }
        return headline;
    }

}