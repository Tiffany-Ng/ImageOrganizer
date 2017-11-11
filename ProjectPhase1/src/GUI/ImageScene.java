package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import ManageImage.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.io.File;

/**
 * GUI of an individual image's information
 */
public class ImageScene {

    /**
     * The image used .
     */
    private ImageFile image;

    private Scene imageScene;

    private GridPane g;

    private  TextArea log;

    private FlowPane f;

    /**The directory that the user first opened */
    private File directory;

    /** The previous picGrid scene */
    private Stage prevScene;

    /**
     * Construct an GUI.ImageScene.
     *
     * @param image
     */
    ImageScene(ImageFile image, File directory, Stage prevScene) {

        this.image = image;

        g = gridSetup();
        imageScene = new Scene(g);

        this.directory = directory;
        this.prevScene = prevScene;

    }

    /**
     * Setup of the whole screen
     *
     * @return GridPane
     */
    private GridPane gridSetup() {

        GridPane layout = new GridPane();
        layout.setHgap(6);
        layout.setVgap(6);
        layout.setPadding(new Insets(10, 10, 10, 10));

        // image in form of a viewable icon
        ImageView icon = new ImageView();
        icon.setFitWidth(720);
        icon.setFitHeight(480);

        // needs the "file://" because image will not understand it is a directory
        icon.setImage(new Image("file:///" + image.getFile().toString()));

        // flowPane for image information
        VBox f = vBoxSetup();

        HBox h = new HBox();
        h.getChildren().add(icon);
        h.getChildren().add(f);
        h.setSpacing(5);

        layout.add(icon, 1,  1, 4, 2);
        layout.add(f, 6,1,2,2);

        // go to main screen
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        back.setOnAction(e -> {

            ImageManager.save();
            PicGrid.picGrid(prevScene, this.directory);

        });

        Text imageName = new Text(image.getName());
        layout.add(imageName, 1, 0, 1, 1);

        TextField newTag = new TextField("Tag Name");
        newTag.setEditable(true);

        Button addTag = new Button("+");
        addTag.setOnAction(e -> {

            image.addTag(newTag.getText());
            addClickableTags();
            updateLog();

        });

        layout.add(newTag, 6, 0, 1, 1);
        layout.add(addTag, 7, 0, 1, 1);

        return layout;

    }

    private HBox headerSetup() {

        HBox header = new HBox();



        return header;

    }

    /**
     * Setup the image's information in a flowPane.
     *
     * @return FlowPane
     */
    private VBox vBoxSetup() {

        // initial values
        VBox flow = new VBox();
        flow.setPadding(new Insets(0, 0, 5, 0));
        flow.setSpacing(10);

        // image directory
        Text directory = new Text();
        directory.setText(image.getDirectory().toString());

        HBox dir = new HBox();
        dir.boundsInParentProperty();
        dir.setMaxWidth(flow.getMaxWidth());

        dir.setStyle("-fx-border-color: gray;");
        dir.getChildren().add(directory);
        flow.getChildren().add(dir);

        // make all the tags
        // sub-flowPane to hold the tags
        f = new FlowPane(Orientation.HORIZONTAL, 7, 5);
        f.setPadding(new Insets(5));
        f.setPrefHeight(480/2.5);
        f = addClickableTags();


        flow.getChildren().add(new ScrollPane(f));

        log = new TextArea();
        // image log
        updateLog();

        log.setWrapText(true);
        log.setEditable(false);
        log.setPrefHeight(480/2);

        flow.getChildren().add(log);

        return flow;

    }

    private FlowPane addClickableTags() {

        List<String> tags = image.getTags();
        f.getChildren().removeAll(f.getChildren());

        ImageView i = new ImageView(new Image("file:///" + "x.jpeg"));

        for (String t : tags) {

            // makes all tags as clickable buttons
            Button tag = new Button(t, i);

            // #TODO make sure tags disappear when clicked, and is visually shown
            tag.setOnAction(e -> {

                image.removeTag(tag.getText());
                f.getChildren().remove(tag);
                updateLog();

            });

            f.getChildren().add(tag);

        }

        return f;

    }

    private void updateLog() {

        Log imageLog = image.getLog();
        StringBuilder logs = new StringBuilder();

        // add all logs as a line
        for (Entry e: imageLog) {

            logs.append(e.toString()).append(" \n");

        }

        // represent log as textArea
        log.setText(logs.toString());


    }

    public Scene getImageScene() { return imageScene; }

}
