
package worksheetout.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    User u1;
    
    @Before
    public void setUp() {
        u1 = new User("tester", "Terttu");
    }
    
    @Test
    public void usernameIsCorrect() {
        assertTrue(u1.getUsername().equals("tester"));
    }
    
    @Test
    public void nameIsCorrect() {
        assertTrue(u1.getName().equals("Terttu"));
    }
    
    @Test
    public void equalWhenSameUsername() {
        User u2 = new User("tester", "Terttu");
        assertTrue(u1.equals(u2));
    }
    
    @Test
    public void nonEqualWhenDifferentUsername() {
        User u2 = new User("martikainen", "Jarkko");
        assertFalse(u1.equals(u2));
    }
    
    @Test
    public void nonEqualWhenDifferentType() {
        Object o = new Object();
        assertFalse(u1.equals(o));
    }
    
    @Test
    public void toStringWorks() {
        assertTrue(u1.toString().equals("Terttu, tester"));
    }
}
