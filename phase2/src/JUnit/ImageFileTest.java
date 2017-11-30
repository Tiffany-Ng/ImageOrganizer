package JUnit;

import ManageImage.ImageFile;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ImageFileTest {

    private static ImageFile imageFile;

    // https://www.petrikainulainen.net/programming/testing/junit-5-tutorial-writing-nested-tests/ Date: Nov 30 2017
    @Nested
    class Test {

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
            if (imageFile.getTags().size() > 0)
                imageFile.removeTag(imageFile.getTags());
        }

        @org.junit.jupiter.api.Test
        void getNameTest() {

            assertEquals("1", imageFile.getName());

        }

        @org.junit.jupiter.api.Test
        void rename() {

            imageFile.rename("newPic");
            assertEquals("newPic", imageFile.getName());

        }

        @org.junit.jupiter.api.Test
        void revertName() {

            imageFile.rename("newPic");
            assertEquals("newPic", imageFile.getName());

            imageFile.revertName(0);
            assertEquals("1", imageFile.getName());


        }

        @org.junit.jupiter.api.Test
        void getPriorNames() {

            imageFile.rename("newPic");
            assertEquals("newPic", imageFile.getName());

            ArrayList<String> namesGotten = imageFile.getPriorNames();
            ArrayList<String> nameGen = new ArrayList<>(Arrays.asList("1.jpg", "newPic.jpg"));

            assertEquals(nameGen, namesGotten);


        }

        @org.junit.jupiter.api.Test
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

        @org.junit.jupiter.api.Test
        void addTag() {
            imageFile.addTag("Tiger");
            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.add("Tiger");
            assertEquals(expectedTags, tags);
        }

        @org.junit.jupiter.api.Test
        void addMultipleTags() {
            // https://stackoverflow.com/questions/21696784/how-to-declare-an-arraylist-with-values Date: Nov 29 2017
            ArrayList<String> toAdd = new ArrayList<>(Arrays.asList("Snow", "Cool"));

            imageFile.addTag(toAdd);
            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.addAll(toAdd);
            assertEquals(expectedTags, tags);
        }

        @org.junit.jupiter.api.Test
        void removeMultipleTags() {
            imageFile.addTag("Tiger");
            imageFile.addTag("Cool");
            imageFile.removeTag("Tiger");

            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.add("Cool");
            assertEquals(expectedTags, tags);
        }

        @org.junit.jupiter.api.Test
        void removeTag1() {
            imageFile.addTag(new ArrayList<>(Arrays.asList("Snow", "Wild", "Awesome")));
            imageFile.removeTag(new ArrayList<>(Arrays.asList("Snow", "Awesome")));

            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>();
            expectedTags.add("Wild");

            assertEquals(expectedTags, tags);

        }

        @org.junit.jupiter.api.Test
        void nameWithTags() {
            imageFile.addTag(new ArrayList<>(Arrays.asList("Snow", "Wild")));
            String actualName = imageFile.nameWithTags();
            String expectedName = "1 @Snow @Wild.jpg";

            assertEquals(expectedName, actualName);
        }
    }

    @Nested
    class TestImageWithTagsAlready {

        @BeforeEach
        void setupEach() {

            try {
                imageFile = new ImageFile(new File("src/JUnit/testImages/baby_snake_deadpan @snake @cute.jpg"));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @org.junit.jupiter.api.Test
        void getTagsTest() {

            ArrayList tags = imageFile.getTags();
            ArrayList<String> expectedTags = new ArrayList<>(Arrays.asList("snake", "cute"));

            assertEquals(expectedTags, tags);

        }

        @org.junit.jupiter.api.Test
        void getNameTest() {

            assertEquals("baby_snake_deadpan", imageFile.getName());

        }
    }


}