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
import java.util.ArrayList;

class PicGrid {
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
                    ImageManager.save();
                    TagManager.save();
                    DirChooser.dirChooser(currentStage);
                });
        pane.getChildren().add(chooseDirectory);

        Label currentDirectory = new Label(directory.toString());
        currentDirectory.setMinWidth(2000);
        pane.getChildren().add(currentDirectory);

        //Loads in tags from tags.ser
        TagManager tm = new TagManager();
        ImageManager im = new ImageManager();
        im.createImagesFromDirectory(directory.toString());
        ArrayList<Button> toAdd = new ArrayList<>();
        for (ImageFile img : im.getImageFilesByDirectory(directory)) {
            Image image = new Image("file:///" + img.getFile().toString(), 200, 200, true, true, true);
            ImageView view = new ImageView(image);
            view.setCache(true);
            view.setCacheHint(CacheHint.SPEED);
            Button viewImage = new Button(img.getName(), view);
            viewImage.setContentDisplay(ContentDisplay.TOP);
            viewImage.setCache(true);
            viewImage.setCacheHint(CacheHint.SPEED);

            viewImage.setOnAction(
                    e -> {
                        ImageScene toScene = new ImageScene(img, directory, currentStage);
                        currentStage.setScene(toScene.getImageScene());
                    });
            toAdd.add(viewImage);
        }
        pane.getChildren().addAll(toAdd);
    }
}
