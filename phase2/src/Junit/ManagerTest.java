package JUnit;

import ManageImage.Manager;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    private Manager<String> manager;

    @BeforeEach
    void setup(){
        manager = new Manager<>("managerTest.ser");
    }

    @Test
    void emptyWhenCreatedTest(){
        assertEquals(0, manager.getAll().size());
    }

    @Test
    void addTest(){
        manager.add("1");
        assertTrue(manager.getAll().contains("1"));
    }

    @Test
    void addRepeatTest(){
        manager.add("1");
        assertFalse(manager.add("1"));
    }

    @Test
    void getAllTest(){
        manager.add("1");
        manager.add("2");
        LinkedList<String> items = new LinkedList<>();
        items.add("1");
        items.add("2");
        assertEquals(items, manager.getAll());
    }

    @Test
    void saveLoadTest(){
        manager.add("1");
        manager.add("2");
        manager.save();
        manager = new Manager<>("managerTest.ser");
        manager.load();
        LinkedList<String> items = new LinkedList<>();
        items.add("1");
        items.add("2");
        assertEquals(items, manager.getAll());
    }
}
