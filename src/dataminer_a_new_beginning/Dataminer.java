package src.dataminer_a_new_beginning;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

public class Dataminer {

    public static void main(String[] args0) throws IOException, ParseException {
        String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Website-crawler\\data";
        HashMap<String, HashMap<String, Integer>> allKeywordRelations = new HashMap<String, HashMap<String, Integer>>();

        HashMap<String, HashMap<String, Integer>> keywordOrigin = new HashMap<String, HashMap<String, Integer>>();

        HashMap<String, Integer> keywordCount = new HashMap<String, Integer>();
        File directory = new File(path);
        Files.createDirectories(Paths.get(path + "\\0 CSV"));
        for (File partyDirectory : directory.listFiles()) {
            if (partyDirectory.isDirectory() && !partyDirectory.getName().equals("0 CSV")) {
                if (!partyDirectory.getName().equals("0 JSON")) {
                    // if (partyDirectory.getName().equals("Piratenpartei")) {
                    Party party = new Party(path, partyDirectory.getName());

                    allKeywordRelations = generateKeywordRelations(allKeywordRelations, party.allKeywordRelations);
                    keywordCount = generateKeywordCount(keywordCount, party.keywordCount);
                    keywordOrigin = generateKeywordOrigin(keywordOrigin, party.keywordOrigin);
                    // }
                }
            }
        }
        generateNodesCSV(keywordCount, keywordOrigin);
        generateEdgesCSV(allKeywordRelations);
        System.out.println("Stop");

    }

    private static void generateNodesCSV(HashMap<String, Integer> keywordCount,
            HashMap<String, HashMap<String, Integer>> keywordOrigin) throws IOException {
        // Methode, die eine CSV für den Graphen erstellt mit allen Knoten als Inhalt

        // Initialisirung des csvStrings mit den Überschriften
        String csvString = "Id\tLabel\tCategory\tCount";

        // Schleife, die durch alle Eintragungen in keywordCount geht
        for (Map.Entry<String, Integer> countEntry : keywordCount.entrySet()) {
            String key = countEntry.getKey(); // key der Eintragung
            int value = countEntry.getValue(); // value der Eintragung

            // Speichert die entsprechende HashMap der Kategorien, passend zum Key
            HashMap<String, Integer> tempMap = keywordOrigin.get(key);
            String highestEntry = getHighestEntry(tempMap);

            csvString += "\n" + key + "\t" + key + "\t" + highestEntry + "\t" + value;
        }

        Path tempPath = Paths.get(
                FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Website-crawler\\data\\0 JSON");
        Files.createDirectories(tempPath);

        File file = new File(tempPath + "/nodes10All.csv");
        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(csvString);
        bw.close();

    }

    private static void generateEdgesCSV(HashMap<String, HashMap<String, Integer>> keywordRelations)
            throws IOException {
        String csvString = "Source\tTarget\tType\tWeight";

        int count = 0;
        for (Map.Entry<String, HashMap<String, Integer>> relationEntry : keywordRelations.entrySet()) {
            count++;
            String key = relationEntry.getKey();
            HashMap<String, Integer> value = getTop10(relationEntry.getValue());

            for (Map.Entry<String, Integer> entry : value.entrySet()) {
                String relation = entry.getKey();
                int weight = entry.getValue();
                csvString += "\n" + key + "\t" + relation + "\tUndirected\t" + weight;
            }
        }

        Path tempPath = Paths.get(
                FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Website-crawler\\data\\0 JSON");
        Files.createDirectories(tempPath);

        File file = new File(tempPath + "/edges10All.csv");
        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write(csvString);
        bw.close();

    }

    public static String getHighestEntry(HashMap<String, Integer> map) {
        String highestEntry = "none";
        int highestValue = 0;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            int value = entry.getValue();
            if (highestValue < value) {
                highestValue = value;
                highestEntry = entry.getKey();
            } else if (highestValue == value) {
                highestEntry = "none";
            }
        }
        return highestEntry;
    }

    public static HashMap<String, Integer> getTop10(HashMap<String, Integer> tempMap) {
        HashMap<String, Integer> sortedMap = new HashMap<String, Integer>();
        int percentage = 10;

        int entryAmount = (int) Math.round((float) tempMap.size() / 100.00 * percentage);

        if (entryAmount < 1) {
            entryAmount = 1;
        }

        // Schleife, die durch alle Einträge durch die Originale HashMap geht
        for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {

            // Überprüft, ob die sortierte HashMap größer ist als die zulässige größe
            if (sortedMap.size() < entryAmount) {
                sortedMap.put(entry.getKey(), entry.getValue());

            } else {
                int count = 0;
                int lowestEntry = 0;
                for (Map.Entry<String, Integer> singleEntry : sortedMap.entrySet()) {

                    count++;

                    if (count <= entryAmount && lowestEntry > singleEntry.getValue() || lowestEntry == 0) {
                        lowestEntry = singleEntry.getValue();
                    }

                    if (entry.getValue() > singleEntry.getValue()) {
                        sortedMap.remove(singleEntry.getKey());
                        sortedMap.put(entry.getKey(), entry.getValue());
                        break;
                    }
                    if (count >= entryAmount && entry.getValue() == lowestEntry) {
                        sortedMap.put(entry.getKey(), entry.getValue());
                        break;
                    }
                    if (count > entryAmount && singleEntry.getValue() < lowestEntry) {
                        sortedMap.remove(singleEntry.getKey());
                        break;
                    }
                }
            }
        }

        return sortedMap;

    }

    public static HashMap<String, HashMap<String, Integer>> generateKeywordRelations(
            HashMap<String, HashMap<String, Integer>> allKeywordRelations,
            HashMap<String, HashMap<String, Integer>> partyKeywordRelations) {

        // Schleife, die durch alle eintragungen in partyKeywordRelations durchschleift
        for (Map.Entry<String, HashMap<String, Integer>> partyEntry : partyKeywordRelations.entrySet()) {
            if (allKeywordRelations.containsKey(partyEntry.getKey())) {// wenn allKeywordRelations den Key von
                                                                       // partyEntry enthält

                HashMap<String, Integer> tempPartyMap = partyEntry.getValue(); // Speichert die Value HashMap in eine
                                                                               // Temporäre HashMap
                HashMap<String, Integer> tempRelationMap = allKeywordRelations.get(partyEntry.getKey());// Speichert die
                                                                                                        // relations zu
                                                                                                        // dem Keyword
                                                                                                        // in eine
                                                                                                        // temporäre
                                                                                                        // HashMap

                for (Map.Entry<String, Integer> tempPartyEntry : tempPartyMap.entrySet()) {
                    if (tempRelationMap.containsKey(tempPartyEntry.getKey())) {
                        tempRelationMap.put(tempPartyEntry.getKey(),
                                tempRelationMap.get(tempPartyEntry.getKey()) + tempPartyEntry.getValue());
                    } else {
                        tempRelationMap.put(tempPartyEntry.getKey(), tempPartyEntry.getValue());
                    }
                }

            } else {
                allKeywordRelations.put(partyEntry.getKey(), partyEntry.getValue());
            }
        }
        return allKeywordRelations;
    }

    public static HashMap<String, Integer> generateKeywordCount(HashMap<String, Integer> keywordCount,
            HashMap<String, Integer> partyKeywordCount) {

        for (Map.Entry<String, Integer> partyEntry : partyKeywordCount.entrySet()) {
            if (keywordCount.containsKey(partyEntry.getKey())) {
                keywordCount.put(partyEntry.getKey(), keywordCount.get(partyEntry.getKey()) + partyEntry.getValue());
            } else {
                keywordCount.put(partyEntry.getKey(), partyEntry.getValue());
            }
        }

        return keywordCount;
    }

    public static HashMap<String, HashMap<String, Integer>> generateKeywordOrigin(
            HashMap<String, HashMap<String, Integer>> allKeywordOrigins,
            HashMap<String, HashMap<String, Integer>> partyKeywordOrigins) {

        // Schleife, die durch alle eintragungen in partyKeywordRelations durchschleift
        for (Map.Entry<String, HashMap<String, Integer>> partyEntry : partyKeywordOrigins.entrySet()) {
            if (allKeywordOrigins.containsKey(partyEntry.getKey())) {// wenn allKeywordRelations den Key von
                                                                     // partyEntry enthält

                HashMap<String, Integer> tempPartyMap = partyEntry.getValue(); // Speichert die Value HashMap in eine
                                                                               // Temporäre HashMap
                HashMap<String, Integer> tempOriginMap = allKeywordOrigins.get(partyEntry.getKey());// Speichert die
                                                                                                    // relations zu
                                                                                                    // dem Keyword
                                                                                                    // in eine
                                                                                                    // temporäre
                                                                                                    // HashMap

                for (Map.Entry<String, Integer> tempPartyEntry : tempPartyMap.entrySet()) {
                    if (tempOriginMap.containsKey(tempPartyEntry.getKey())) {
                        tempOriginMap.put(tempPartyEntry.getKey(),
                                tempOriginMap.get(tempPartyEntry.getKey()) + tempPartyEntry.getValue());
                    } else {
                        tempOriginMap.put(tempPartyEntry.getKey(), tempPartyEntry.getValue());
                    }
                }

            } else {
                allKeywordOrigins.put(partyEntry.getKey(), partyEntry.getValue());
            }
        }
        return allKeywordOrigins;
    }
}