package JUnit;

import ManageImage.Entry;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {

    private Entry entry;
    private Date date;

    @BeforeEach
    void setup(){
        entry = new Entry("Entry message");
        date = new Date();
    }

    @Test
    void toStringTest(){
        assertEquals(date.toString() + " - Entry message", entry.toString());
    }
}