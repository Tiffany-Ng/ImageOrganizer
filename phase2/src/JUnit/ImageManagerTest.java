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


    }

    @Test
    void getImageFilesInSubDirectory() {

        ArrayList<ImageFile> k = ImageManager.getImageFilesInSubDirectory(new File("src/JUnit/testImages"));
        ArrayList<ImageFile> d = new ArrayList<>();

        try {
            d.add(new ImageFile(new File("src/JUnit/testImages/sub/pokemon.jpg")));
        } catch (Exception e) {

            e.printStackTrace();

        }

        assertEquals(d, k);

    }

    @Test
    void getImageFilesInParentDirectory() {
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