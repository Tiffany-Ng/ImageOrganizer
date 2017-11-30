package guiView;

import ManageImage.ImageFile;
import guiController.DirController;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Controls all the scenes in the program and switched between them without having to make new instances of each scene.
 */
public class SceneManager {

    /**
     * The main stage.
     */
    private static Stage stage;

    /**
     * PicGrid object.
     */
    private static PicGridView picGrid;

    /**
     * ImageView object.
     */
    private static ImageSceneView imageScene;

    /**
     * Create a manager, to control changes in Scenes.
     *
     * @param stage main Stage
     */
    SceneManager(Stage stage) {

        SceneManager.stage = stage;
        picGrid = new PicGridView(stage);
        imageScene = new ImageSceneView(stage);

    }

    /**
     * Swap to the PicGrid.
     *
     * @param dir File
     */
    static void swapToPicGrid(File dir) {

        stage.setMaximized(true);
        stage.setWidth(1325);
        stage.setHeight(750);

        picGrid.initialize(dir);
        picGrid.picGrid();

    }

    /**
     * Swap to the ImageScene.
     *
     * @param image ImageFile
     * @param dir File
     */
    public static void swapToImageScene(ImageFile image, File dir) {

        stage.setMaximized(true);
        stage.setWidth(1720);
        stage.setHeight(750);

        try {

            imageScene.initialize(image, dir);

        } catch (IOException e) {

            e.printStackTrace();

        }

        stage.setScene(imageScene.getImageScene());

    }

    /**
     * Brings up the directory chooser. Lets user choose a directory.
     */
    void dirChooser() {

        DirController.dirChooser(stage);

    }

}
