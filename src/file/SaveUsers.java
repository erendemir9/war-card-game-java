package file;

import player.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveUsers {
    private String fileName ="saveuser.txt";

    public void save(User user) {
       File file=new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // in 1 line "username name"
            writer.write(user.getId() + " " + user.getName());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}