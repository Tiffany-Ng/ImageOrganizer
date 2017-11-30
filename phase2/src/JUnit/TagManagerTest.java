package JUnit;

import ManageImage.TagManager;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class TagManagerTest {

    @Test
    void startEmptyTest(){
        assertEquals(0, TagManager.getTags().size());
    }

    @Test
    void addTagTest(){
        TagManager.add("1");
        assertTrue(TagManager.getTags().contains("1"));
    }

    /**
     * TagManager does not add repeated tags.
     * <p>Since TagManager is a static variable, it already contains "1".</p>
     */
    @Test
    void addRepeatTest(){
        TagManager.add("1");
        assertEquals(1, TagManager.getTags().size());
    }

    /**
     * TagManager does not add repeated tags.
     * <p>Since TagManager is a static variable, it already contains "1"</p>
     */
    @Test
    void getTagsTest(){
        TagManager.add("1");
        TagManager.add("2");
        LinkedList<String> items = new LinkedList<>();
        items.add("1");
        items.add("2");
        assertEquals(items, TagManager.getTags());
    }
}
