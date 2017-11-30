package guiView;

import ManageImage.ImageFile;
import guiController.dirController;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SceneManager {

    private static Stage stage;
    private static PicGridView picGrid;
    private static ImageSceneView imageScene;

    SceneManager(Stage stage) {

        SceneManager.stage = stage;
        picGrid = new PicGridView(stage);
        imageScene = new ImageSceneView(stage);

    }

    static void swapToPicGrid(File dir) {

        stage.setMaximized(true);
        stage.setWidth(1325);
        stage.setHeight(750);

        picGrid.initialize(dir);
        picGrid.picGrid();

    }

    public static void swapToImageScene(ImageFile image, File dir) {

        stage.setMaximized(true);
        stage.setWidth(1385);
        stage.setHeight(750);

        try {

            imageScene.initialize(image, dir);

        } catch (IOException e) {

            e.printStackTrace();

        }

        stage.setScene(imageScene.getImageScene());

    }

    void dirChooser() {

        dirController.dirChooser(stage);

    }

}
