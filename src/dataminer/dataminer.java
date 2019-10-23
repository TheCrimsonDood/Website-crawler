package src.dataminer;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;
import javax.swing.filechooser.FileSystemView;

public class Dataminer {

    Dataminer() throws IOException, ParseException, ScriptException, NoSuchMethodException {

        File path = new File(
                // sets search path for party directories
                FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Website-crawler\\data");
        File[] parties = path.listFiles();// Listet alle Datein in path auf
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory(); // filter for only directories
            }
        };
        parties = path.listFiles(fileFilter); // Filtert nur nach Ordnern

        if (parties.length == 0) { // Sicherheitsüberprüfung, ob Ordner existieren
            System.out.println("No directory found.");
        } else {
            for (File singleFile : parties) {

                String filename = singleFile.getName();// Speichert den Namen der Partei

                // Erstellt eine neue Partei mit dem Namen des Ordners
                Party party = new Party(filename, path.getPath());

                System.out.println(filename + " found.");

                //Ruft generateJSON auf und befüllt den String mit einer fertigen JSON datei
                String JSONString = generateJSON(party);

                //Erstellt eine Datei mit dem Parteiname innerhalb des My Document Ordner
                String absoluteFilePath = path.getPath() + "\\" + party.name + ".json";
                File file = new File(absoluteFilePath);

                if (file.createNewFile()) {
                    System.out.println(absoluteFilePath + " successfully created.");
                } else {
                    System.out.println(absoluteFilePath + " already exists.");
                }

                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(path.getPath() + "\\" + party.name + ".json"));

                System.out.println("Wrote String into file.");

                writer.write(JSONString);
                writer.close();

                System.out.println("Completed mining for " + party.name);
            }
        }
    }

    private String generateJSON(Party party) {
        //Erhält eine Partei und schreibt mit den Werten aus dem Objekt einen String im JSON Format

        String JSONString = "{\n";
        JSONString = JSONString + "\t\"Partei\": \"" + party.name + "\",\n";
        JSONString = JSONString + "\t\"Anzahl der Artikel\":\"" + party.articleCount + "\",\n";

        JSONString = JSONString + "\t\"Kategorien\":{\n";
        for (Map.Entry<String, Integer> entry : party.categorys.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            JSONString = JSONString + "\t\t\"" + key + "\":" + value + ",\n";
        }
        JSONString = JSONString + "\t},\n";

        JSONString = JSONString + "\t\"Artikel vor der EU-Wahl\":\"" + party.beforeEU + "\",\n";
        JSONString = JSONString + "\t\"Artikel nach der EU-Wahl\":\"" + party.afterEU + "\",\n";
        JSONString = JSONString + "\t\"Artikel während der EU-Wahl\":\"" + party.whileEU + "\",\n";
        JSONString = JSONString + "\t\"Durchschnittliche Artikellänge in Zeichen\":\"" + party.averageArticleLength+ "\",\n";

        JSONString = JSONString + "\t\"Veröffentlichungen nach Monaten\":{\n";
        for (Map.Entry<String, Month> entry : party.releaseMonth.entrySet()) {
            HashMap<String, Integer> articlePerDay = entry.getValue().articlePerDay;
            String key = entry.getKey();
            JSONString = JSONString + "\t\t\"" + key + "\":{\n";
            for (Map.Entry<String, Integer> monthList : articlePerDay.entrySet()) {
                String month = monthList.getKey();
                int count = monthList.getValue();

                JSONString = JSONString + "\t\t\t\"" + month + "\":\"" + count + "\",\n";
            }
            JSONString = JSONString + "\t\t},\n";
        }
        JSONString = JSONString + "\t},\n";

        JSONString = JSONString + "\t\"Schlagwortanzahl\":{\n";
        for (Map.Entry<String, Integer> entry : party.keywordCount.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            JSONString = JSONString + "\t\t\"" + key + "\":" + value + ",\n";
        }
        JSONString = JSONString + "\t},\n";

        JSONString = JSONString + "\t\"Autoren\":{\n";
        for (Map.Entry<String, Integer> entry : party.authorCount.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            JSONString = JSONString + "\t\t\"" + key + "\":" + value + ",\n";
        }
        JSONString = JSONString + "\t},\n";

        JSONString = JSONString + "\t\"Wortanzahl\":{\n";
        for (Map.Entry<String, Integer> entry : party.exactWordCount.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            JSONString = JSONString + "\t\t\"" + key + "\":" + value + ",\n";
        }
        JSONString = JSONString + "\t},\n";

        JSONString = JSONString + "\t\"Zeitpunkte der Veröffentlichungen\":[\n";
        for (String entry : party.timeOfRelease) {
            JSONString = JSONString + "\t\t\"" + entry + "\",\n";
        }
        JSONString = JSONString + "\t]\n";
        JSONString = JSONString + "}\n";

        JSONString = JSONString.replaceAll(",\n\t}", "\n\t}");
        JSONString = JSONString.replaceAll(",\n\t\t}","\n\t\t}");
        JSONString = JSONString.replaceAll(",\n\t]","\n\t]");

        return JSONString;
    }
}