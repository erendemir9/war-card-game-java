package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadUsers {
    private String fileName="saveuser.txt";
     //getting every line inside saveuser.txt then returning them as a list

    public List<String> load() {

        List<String> lines = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            // if there is no file, return empty list
            return lines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}