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
                if (partyDirectory.getName().equals("FDP") || partyDirectory.getName().equals("Piratenpartei")
                        || partyDirectory.getName().equals("AFD")) {
                    Party party = new Party(path, partyDirectory.getName());

                    allKeywordRelations = generateKeywordRelations(allKeywordRelations, party.allKeywordRelations);
                    keywordCount = generateKeywordCount(keywordCount, party.keywordCount);
                    keywordOrigin = generateKeywordOrigin(keywordOrigin, party.keywordOrigin);
                }
            }
        }
        generateJSON(keywordCount, allKeywordRelations, path);
        System.out.println("Stop");

    }

    private static void generateJSON(HashMap<String, Integer> keywordCount,
            HashMap<String, HashMap<String, Integer>> allKeywordRelations, String path) throws IOException {
String jsonString = "{\n\t\"nodes\": [\n";
                
        String nodeString = "";
        int count = 0;
        for (Map.Entry<String, Integer> countEntry : keywordCount.entrySet()) {
            count++;
            nodeString = nodeString + "\t\t{\n";
            nodeString = nodeString + "\t\t\t\"id\": \"" + countEntry.getKey() + "\",\n";
            nodeString = nodeString + "\t\t\t\"name\": \"" + countEntry.getKey() + "\",\n";
            nodeString = nodeString + "\t\t\t\"val\": " + countEntry.getValue() + "\n";
            nodeString = nodeString + "\t\t}";
            if (count != keywordCount.size()) {
                nodeString += ",\n";
            } else {
                nodeString += "\n";
            }
        }

        jsonString += nodeString + "\t],\n\"links\": [\n";
        count = 0;
        int keywordCounter = 0;
        String linkString = "";
        for (Map.Entry<String, HashMap<String, Integer>> keywordEntry : allKeywordRelations.entrySet()) {
            keywordCounter++;
            count = 0;
            HashMap<String, Integer> tempMap = keywordEntry.getValue();
            for (Map.Entry<String, Integer> relationEntry : tempMap.entrySet()) {
                count++;
                linkString = linkString + "\t\t{\n";
                linkString = linkString + "\t\t\t\"source\": \"" + keywordEntry.getKey() + "\",\n";
                linkString = linkString + "\t\t\t\"target\": \"" + relationEntry.getKey() + "\"\n";
                linkString = linkString + "\t\t}";
                if (count != tempMap.size() || keywordCounter != allKeywordRelations.size()) {
                    linkString += ",\n";
                } else {
                    linkString += "\n";
                }

            }
        }
        jsonString += linkString + "\t]\n}";

        Path tempPath = Paths.get(path + "\\0 JSON\\");
        Files.createDirectories(tempPath);
        
        File file = new File(path + "\\0 JSON\\" + "nodes" + ".json");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(jsonString);
            bw.close();
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