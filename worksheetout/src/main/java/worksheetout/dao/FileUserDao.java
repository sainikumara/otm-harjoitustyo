package worksheetout.dao;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import worksheetout.domain.User;

public class FileUserDao implements UserDao {
    private List<User> users;
    private String fileName;

    public FileUserDao(String newFileName) throws Exception {
        users = new ArrayList<>();
        this.fileName = newFileName;
        try {
            this.scanUserFile();
        } catch (Exception e) {
            FileWriter writer = new FileWriter(new File(this.fileName));
            writer.close();
        }     
    }
    
    private void scanUserFile() throws Exception {
        Scanner reader = new Scanner(new File(this.fileName));
        while (reader.hasNextLine()) {
            String[] parts = reader.nextLine().split(";");
            User u = new User(parts[0], parts[1]);
            users.add(u);
        }
    }
    
    private void save() throws Exception {
        try (FileWriter writer = new FileWriter(new File(fileName))) {
            for (User user : users) {
                writer.write(user.getUsername() + ";" + user.getSpreadsheetId()+ "\n");
            }
        } 
    }
    
    @Override
    public List<User> getAll() {
        return users;
    }
    
    @Override
    public User findByUsername(String username) {
        return users.stream()
            .filter(u->u.getUsername()
            .equals(username))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public User create(User user) throws Exception {
        users.add(user);
        save();
        return user;
    }    
}
