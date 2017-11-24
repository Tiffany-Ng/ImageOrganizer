package guiView;

import ManageImage.ImageFile;
import guiController.dirController;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SceneManager {

    private static Stage stage;
    private static PicGrid picGrid;
    private static ImageScene imageScene;

    public SceneManager(Stage stage) {

        SceneManager.stage = stage;
        picGrid = new PicGrid(stage);
        imageScene = new ImageScene(stage);

    }

    public static void swapToPicGrid(File dir) {

        stage.setMaximized(true);
        stage.setWidth(1325);
        stage.setHeight(750);

        picGrid.initialize(dir);
        picGrid.picGrid();

    }

    public static void swapToImageScene(ImageFile image, File dir) {

        stage.setMaximized(true);
        stage.setWidth(1325);
        stage.setHeight(750);

        try {

            imageScene.initialize(image, dir);

        } catch (IOException e) {

            e.printStackTrace();

        }

        stage.setScene(imageScene.getImageScene());

    }

    public void dirChooser() {

        dirController.dirChooser(stage);

    }

}
