package JUnit;

import ManageImage.ImageFile;
import ManageImage.ImageManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ImageManagerTest {

    @BeforeAll
    static void setup() {
        ImageManager.createImagesFromDirectory("src/JUnit/testImages");
    }

    @Test
    void getImageFilesInSubDirectory() {

        ArrayList<ImageFile> filesInSubDirectory = ImageManager.getImageFilesInSubDirectory(new File("src/JUnit/testImages"));
        ArrayList<ImageFile> expectedFiles = new ArrayList<>();

        try {
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/sub/pokemon.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedFiles, filesInSubDirectory);

    }

    @Test
    void getImageFilesInParentDirectory() {

        ArrayList<ImageFile> filesInParentDirectory = ImageManager.getImageFilesInParentDirectory(new File("src/JUnit/testImages"));
        ArrayList<ImageFile> expectedFiles = new ArrayList<>();

        try {
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/1.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/1copy.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/3.jpeg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/3copy.jpeg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/4.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/4copy.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/baby_snake_deadpan @snake @cute.jpg")));
            expectedFiles.add(new ImageFile(new File("src/JUnit/testImages/filterJUnitTest.jpg")));



        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(expectedFiles, filesInParentDirectory);
    }

    @Test
    void createImagesFromDirectory() {
    }

    @Test
    void deleteGlobalTag() {
    }

    @Test
    void addGlobalTag() {
    }

}