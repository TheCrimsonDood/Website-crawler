package src.dataminer;

import java.io.*;
import java.text.ParseException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Dataminer {

    Dataminer() throws IOException, ParseException, ScriptException {
  
          
        File path = new File("src/data/");                                  //sets search path for party directories
        File[] parties = path.listFiles();
        FileFilter fileFilter = new FileFilter(){
            public boolean accept (File file){
                return file.isDirectory();                                  //filter for only directories
            }
        };
        parties = path.listFiles(fileFilter);

        if (parties.length == 0){
            System.out.println("No directory found.");          
        } else{
            for (int i = 0; i<parties.length; i++ ){


                File partynameFile = parties[i];
                String partyname = partynameFile.toString();
                partyname = partyname.substring(9);                         //extracts Party name from path
                
                System.out.println(partyname+" found.");                    //prints all found party names

                Party party = new Party(partyname, "src/data/");            //creates new Party object

                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("JavaScript");
                if (!(engine instanceof Invocable)) {
                    System.out.println("Invoking methods is not supported.");
                    return;
                }
                Invocable inv = (Invocable) engine;
                String scriptPath = "src/dataminer/JSONGenerator.js";

                engine.eval("load('"+scriptPath+"')");

                Object JSONGenerator = engine.get("JSONGenerator");         
                
               
                
                try {
                    Object createJSONFile = inv.invokeMethod(JSONGenerator, "createJSONFile", partyname, party.articleCount, party.categorys, party.beforeEU, party.afterEU, party.whileEU, party.averageArticleLength, party.releaseMonth, party.keywordCount, party.authorCount, party.exactWordCount, party.allKeywordRelations, party.keywordOrigin);
                    String JSONString = String.valueOf(createJSONFile);

                    String fileSeparator = System.getProperty("file.separator");
                    String absoluteFilePath = "src"+fileSeparator+"data"+fileSeparator+partyname+".json";
                    File file = new File(absoluteFilePath);

                    if(file.createNewFile()){
                        System.out.println(absoluteFilePath+" successfully created.");
                    }else{
                        System.out.println(absoluteFilePath+" already exists.");
                    }

                    BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/"+partyname+".json"));

                    writer.write(JSONString);
                    System.out.println("Wrote String into file.");

                    BufferedReader br = new BufferedReader(new FileReader("src/data/"+partyname+".json"));
                    String line = "";
                    StringBuffer sb = new StringBuffer();
                    while ((line = br.readLine()) != null) {
                        line = line.replace(",", ",\n");
                        line = line.replace("\"","");
                        sb.append(line);
                    }
                    br.close();
                    writer.write(sb.toString(), 0, sb.length());
                    writer.close();
                } catch (NoSuchMethodException e) {
					e.printStackTrace();
                }

                

                System.out.println("Completed mining for "+partyname);
            }
        }
    }
}