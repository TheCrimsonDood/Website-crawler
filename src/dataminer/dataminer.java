package src.dataminer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Dataminer {

    Dataminer() throws IOException, ParseException, ScriptException, NoSuchMethodException {

        File path = new File("src/data/"); // sets search path for party directories
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
                Party party = new Party(filename, path.getPath()); // Erstellt eine neue Partei mit dem Namen des
                                                                    // Ordners

                System.out.println(filename + " found.");

                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("JavaScript");
                // if (!(engine instanceof Invocable)) {
                // System.out.println("Invoking methods is not supported.");
                // return;
                // }
                engine.eval(Files.newBufferedReader(Paths.get("src/dataminer/JSONGenerator.js")));
                Invocable inv = (Invocable) engine;

                String JSONString = (String)inv.invokeFunction("createJSONFile", party.name, party.articleCount, party.categorys,
                        party.beforeEU, party.afterEU, party.whileEU, party.averageArticleLength, party.releaseMonth,
                        party.keywordCount, party.authorCount, party.exactWordCount, party.allKeywordRelations,
                        party.keywordOrigin);

                String absoluteFilePath = "src/data/" + party.name + ".json";
                File file = new File(absoluteFilePath);

                if (file.createNewFile()) {
                    System.out.println(absoluteFilePath + " successfully created.");
                } else {
                    System.out.println(absoluteFilePath + " already exists.");
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/" + party.name + ".json"));

                // writer.write(JSONString);
                System.out.println("Wrote String into file.");

                // BufferedReader br = new BufferedReader(new
                // FileReader("src/data/"+partyname+".json"));
                // BufferedReader br = new BufferedReader(new FileReader("src/data/" + partyname
                // + ".json"));
                String line = "";
                // StringBuffer sb = new StringBuffer();
                // while ((line = br.readLine()) != null) {
                // line = line.replace(",", ",\n");
                // line = line.replace("\"","");
                // sb.append(line);
                // }
                // JSONString = JSONString.replaceAll(",", ",\n");
                // JSONString = JSONString.replaceAll("\\\"", "");

                // br.close();
                writer.write(JSONString);
                writer.close();

                System.out.println("Completed mining for " + party.name);
            }
        }
    }
}