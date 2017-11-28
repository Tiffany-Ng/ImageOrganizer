package JUnit;

import ManageImage.ImageFile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ImageFileTest {

    private static ImageFile k;

    @BeforeAll
    static void setup() {

        try {
            k = new ImageFile(new File("src/JUnit/testImages/1.jpeg"));
        } catch (Exception e){

            e.printStackTrace();

        }


    }

    @Test
    void getNameTest() {

        assertEquals("1", k.getName());

    }

    @Test
    void rename() {

        k.rename("newPic");
        assertEquals("newPic", k.getName());

    }

    @Test
    void revertName() {

        k.revertName(0);
        assertEquals("1", k.getName());

    }

    @Test
    void getPriorNames() {

        ArrayList<String> namesGotten = k.getPriorNames();
        ArrayList<String> nameGen = new ArrayList<>();
        nameGen.add("newPic.jpeg");
        nameGen.add("1.jpeg");

        assertEquals(nameGen, namesGotten);


    }

    @Test
    void move() {

        try {
            k.move(new File("src/JUnit/testImages/sub"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(new File("src/JUnit/testImages/sub"), k.getDirectory());

        try {
            k.move(new File("src/JUnit/testImages"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void addTag() {
    }

    @Test
    void addTag1() {
    }

    @Test
    void removeTag() {
    }

    @Test
    void removeTag1() {
    }

    @Test
    void nameWithTags() {
    }

}