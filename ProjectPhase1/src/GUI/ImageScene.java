package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import ManageImage.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Construct an GUI.ImageScene.
     *
     * @param image
     */
    public ImageScene(ImageFile image) {

        this.image = image;

        g = gridSetup();
        imageScene = new Scene(g);

    }

    /**
     * Setup of the whole screen
     *
     * @return GridPane
     */
    private GridPane gridSetup() {

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(0, 10, 10, 10));

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

        layout.add(h, 1,  1, 4, 2);

        // go to main screen
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        // #TODO setup the action to go back to main screen
        back.setOnAction(e -> {



        });

        return layout;

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


        // sub-flowPane to hold the tags
        FlowPane f = new FlowPane(Orientation.HORIZONTAL, 7, 5);
        f.setPadding(new Insets(5));
        f.setPrefHeight(480/2.5);

        // make all the tags
        List<String> tags = image.getTags();
        addClickableTags(tags, f);

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

    private void addClickableTags(List<String> tags, FlowPane f) {

        for (String t : tags) {

            // makes all tags as clickable buttons
            Button tag = new Button(t);

            // #TODO make sure tags disappear when clicked, and is visually shown
            tag.setOnAction(e -> {

                image.removeTag(tag.getText());
                f.getChildren().remove(tag);
                updateLog();

            });

            f.getChildren().add(tag);

        }

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
