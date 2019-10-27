package src.dataminer_a_new_beginning;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import javax.swing.filechooser.FileSystemView;

public class Dataminer {

    public static void main(String[] args0) throws IOException, ParseException {
        String path = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + "\\Website-crawler\\data";

        File directory = new File(path);
        Files.createDirectories(Paths.get(path + "\\0 CSV"));
        // partyDirectory.getName() != "csv" &&
        for (File partyDirectory : directory.listFiles()) {
            String test = partyDirectory.getName();
            if (partyDirectory.isDirectory() && !partyDirectory.getName().equals("0 CSV")) {
                // if (partyDirectory.getName().equals("Piratenpartei")) {
                    new Party(path, partyDirectory.getName());

                // }
            }
        }

    }
}