package worksheetout.domain;

/**
 * A class representing an individual user
 */

public class User {
    private String name;
    private String username;

    public User(String username, String name) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) {
            return false;
        }
        
        User other = (User) obj;
        return username.equals(other.username);
    }
    
    @Override
    public String toString() {
        return this.name + ", " + this.username;
    }
}
