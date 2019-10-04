package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class directorys {

    public static void createDirectory(String pathString) {

        // Erstellt die Ordnerstruktur an dem angegebenen Pfad
        Path path = Paths.get(pathString);
        try {
            Files.createDirectories(path);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}