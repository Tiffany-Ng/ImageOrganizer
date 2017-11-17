package GUI;

import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ManageImage.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Shows a list of thumbnails of all images under the directory.
 */
class PicGrid {

    /**
     * Sets the pane and ImageFiles in a grid like format.
     *
     * @param currentStage the Stage that the user is in
     * @param directory the chosen directory
     */
    static void picGrid(Stage currentStage, File directory) {

        currentStage.setTitle("Image Viewer - List images");

        FlowPane pane = new FlowPane();
        pane.setPadding(new Insets(20, 20, 5, 20));
        pane.setVgap(15);
        pane.setHgap(15);
        pane.setPrefWrapLength(300);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        currentStage.setMaximized(true);
        scrollPane.setMinViewportWidth(currentStage.getWidth());
        scrollPane.setMinViewportHeight(currentStage.getHeight());

        Scene scene = new Scene(scrollPane);
        currentStage.setScene(scene);

        Button chooseDirectory = new Button("Select directory");
        chooseDirectory.setOnAction(
                e -> {
                    DirChooser.dirChooser(currentStage);
                });
        pane.getChildren().add(chooseDirectory);

        Label currentDirectory = new Label(directory.toString());
        currentDirectory.setMinWidth(2000);
        pane.getChildren().add(currentDirectory);

        ArrayList<Button> toAdd = new ArrayList<>();
        for (ImageFile img : ImageManager.getImageFilesByDirectory(directory)) {

            Image image = new Image("file:///" + img.getFile().toString(), 200, 200, true, true, true);
            ImageView view = new ImageView(image);

            // Source: https://stackoverflow.com/questions/18911186/how-do-setcache-and-cachehint-work-together-in-javafx (Date: Nov 9, 2017)
            view.setCache(true);
            view.setCacheHint(CacheHint.SPEED);

            Button viewImage = new Button(img.getName(), view);
            viewImage.setContentDisplay(ContentDisplay.TOP);

            // Source: https://stackoverflow.com/questions/18911186/how-do-setcache-and-cachehint-work-together-in-javafx (Date: Nov 9, 2017)
            viewImage.setCache(true);
            viewImage.setCacheHint(CacheHint.SPEED);

            viewImage.setOnAction(
                    e -> {
                        ImageScene toScene = null;
                        try {
                            toScene = new ImageScene(img, directory, currentStage);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        currentStage.setScene(toScene.getImageScene());
                    });
            toAdd.add(viewImage);
        }
        pane.getChildren().addAll(toAdd);
    }
}
