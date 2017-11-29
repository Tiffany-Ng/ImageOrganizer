package JUnit;

import ManageImage.ImageFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ImageFileTest {

    private static ImageFile imageFile;

    @BeforeAll
    static void setup() {

        try {
            imageFile = new ImageFile(new File("src/JUnit/testImages/1.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @BeforeEach
    void setupEach() {

        try {
            imageFile = new ImageFile(new File("src/JUnit/testImages/1.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @AfterEach
    void afterEach() {
        imageFile.rename("1");
        if(imageFile.getTags().size() >0)
        imageFile.removeTag(imageFile.getTags());
    }

    @Test
    void getNameTest() {

        assertEquals("1", imageFile.getName());

    }

    @Test
    void rename() {

        imageFile.rename("newPic");
        assertEquals("newPic", imageFile.getName());

    }

    @Test
    void revertName() {

        imageFile.rename("newPic");
        assertEquals("newPic", imageFile.getName());

        imageFile.revertName(0);
        assertEquals("1", imageFile.getName());


    }

    @Test
    void getPriorNames() {

        imageFile.rename("newPic");
        assertEquals("newPic", imageFile.getName());

        ArrayList<String> namesGotten = imageFile.getPriorNames();
        ArrayList<String> nameGen = new ArrayList<>(Arrays.asList("1.jpg", "newPic.jpg"));

        assertEquals(nameGen, namesGotten);


    }

    @Test
    void move() {

        try {
            imageFile.move(new File("src/JUnit/testImages/sub"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(new File("src/JUnit/testImages/sub"), imageFile.getDirectory());

        try {
            imageFile.move(new File("src/JUnit/testImages"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void addTag() {
        imageFile.addTag("Tiger");
        ArrayList tags = imageFile.getTags();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add("Tiger");
        assertEquals(expectedTags, tags);
    }

    @Test
    void addTag1() {
        // https://stackoverflow.com/questions/21696784/how-to-declare-an-arraylist-with-values Date: Nov 29 2017
        ArrayList<String> toAdd = new ArrayList<>(Arrays.asList("Snow", "Cool"));

        imageFile.addTag(toAdd);
        ArrayList tags = imageFile.getTags();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.addAll(toAdd);
        assertEquals(expectedTags, tags);
    }

    @Test
    void removeTag() {
        imageFile.addTag("Tiger");
        imageFile.addTag("Cool");
        imageFile.removeTag("Tiger");

        ArrayList tags = imageFile.getTags();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add("Cool");
        assertEquals(expectedTags, tags);
    }

    @Test
    void removeTag1() {
        imageFile.addTag(new ArrayList<>(Arrays.asList("Snow", "Wild", "Awesome")));
        imageFile.removeTag(new ArrayList<>(Arrays.asList("Snow", "Awesome")));

        ArrayList tags = imageFile.getTags();
        ArrayList<String> expectedTags = new ArrayList<>();
        expectedTags.add("Wild");

        assertEquals(expectedTags, tags);

    }

    @Test
    void nameWithTags() {
        imageFile.addTag(new ArrayList<>(Arrays.asList("Snow", "Wild")));
        String actualName = imageFile.nameWithTags();
        String expectedName = "1 @Snow @Wild.jpg";

        assertEquals(expectedName, actualName);
    }

}