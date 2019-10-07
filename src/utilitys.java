package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class utilitys {

    public static void createDirectory(String pathString) {
        // Erstellt die Ordnerstruktur an dem angegebenen Pfad

        // --- Variablen ---
        Path path = Paths.get(pathString);

        // --- Funktionalität ---
        try {
            Files.createDirectories(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Date stringToDate(String stringDate) {
        // Transformiert ein String in ein Datum

        // --- Variablen ---
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        stringDate = stringDate.replace(".", "-");
        Date date = null;

        // --- Funktionalität ---
        try {
            date = sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void saveToFile(URL url, String path) {
        // Speichert den Inhalt des Buffered Readers in eine .txt datei

        // --- Variablen ---
        String line;
        BufferedReader br = crawler.createBR(url);
        String fileTitle = crawler.getHeadline(br);
        
        br = crawler.createBR(url); // Erneutes erstellen des BR, da er im getHeadLine schon aufgebraucht wurde.
        // String completePath = path + "/" + fileTitle + ".txt";
        BufferedWriter bw;

        // --- Funktionalität ---
        try {
            File file = new File(path, fileTitle + ".txt");
            if (!file.exists()) {
            FileWriter writer = new FileWriter(file);
            bw = new BufferedWriter(writer);
                while ((line = br.readLine()) != null) {

                    bw.write(line);
                }
                br.close();
            }else{
                System.out.println(fileTitle + " existiert bereits");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}