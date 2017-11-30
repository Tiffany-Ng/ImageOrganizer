package JUnit;

import ManageImage.Entry;
import ManageImage.Log;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {

    private Log log;

    @BeforeEach
    void setup(){
        log = new Log();
    }

    @Test
    void constructorNoEntryTest(){
        int logCount = 0;
        for(Entry ignored : log){
            logCount++;
        }
        assertEquals(logCount, 0);
    }

    @Test
    void addTest(){
        Entry entryRaw = new Entry("Unit Test Message");
        log.addEntry(entryRaw);
        for(Entry entry : log){
            assertEquals(entry, entryRaw);
        }
    }

    @Test
    void iteratorTest(){
        LinkedList<Entry> entries = new LinkedList<>();
        entries.add(new Entry("1"));
        entries.add(new Entry("2"));
        entries.add(new Entry("3"));
        entries.add(new Entry("4"));
        for(Entry entry : entries){
            log.addEntry(entry);
        }
        int counter = 0;
        for(Entry entry : log){
            assertEquals(entry, entries.get(counter));
            counter++;
        }
        assertEquals(counter, 4);
    }
}