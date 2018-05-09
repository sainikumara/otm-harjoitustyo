package worksheetout.domain;

/**
 * A class representing an individual user
 */

public class User {
    private String username;
    private String spreadsheetId;

    public User(String name, String newSpreadsheetId) {
        this.username = name;
        this.spreadsheetId = newSpreadsheetId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSpreadsheetId() {
        return this.spreadsheetId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        
        User other = (User) obj;
        return this.username.equals(other.username);
    }
    
    @Override
    public String toString() {
        return this.username + ", " + this.spreadsheetId;
    }
}
