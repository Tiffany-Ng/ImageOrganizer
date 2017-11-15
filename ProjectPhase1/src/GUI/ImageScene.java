package GUI;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import ManageImage.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.LinkedList;
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

    private ComboBox<String> imageNames;


    /**
     * Construct an GUI.ImageScene.
     *
     * @param image
     */
    public ImageScene(ImageFile image, File directory, Stage prevScene) {

        this.image = image;

        g = gridSetup();
        imageScene = new Scene(g);

        this.directory = directory;
        this.prevScene = prevScene;

        //prevScene.sizeToScene();
        prevScene.setWidth(imageScene.getWidth());
        prevScene.setHeight(imageScene.getHeight());

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

        Text instruction = new Text();
        instruction.setText("Wanna delete a tag?,  SELECT IT!");
        instruction.setFont(Font.font(java.awt.Font.SERIF, 16));
        instruction.setFill(Color.DARKBLUE);
        layout.add(instruction, 6, 1);


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
        layout.add(f, 6,2,2,2);

        // go to main screen
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        back.setOnAction(e -> {
            PicGrid.picGrid(prevScene, this.directory);
        });

        imageNames = new ComboBox<>();
        imageNameUpdate();
        imageNames.setMaxWidth(720);
        imageNames.getSelectionModel().selectFirst();

        Button revertName = new Button("Revert");
        revertName.setOnAction(event -> {

            if (!image.nameWithTags().equals(imageNames.getValue())) {
                image.revertName(imageNames.getItems().size() -
                        ((imageNames.getItems().indexOf(imageNames.getValue()) == -1) ?
                                0 : imageNames.getItems().indexOf(imageNames.getValue())) - 1);
                updateLog();
                addClickableTags();
                imageNameUpdate();
            }

        });

        HBox imageName = new HBox();
        imageName.getChildren().addAll(imageNames, revertName);
        imageName.setSpacing(5.0);

        layout.add(imageName, 1, 0, 1, 1);

        ComboBox newTag = new ComboBox();
        newTag.setValue("Tag name");
        newTag.setEditable(true);


        newTag.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            LinkedList<String> relatedTags = new LinkedList<>();
            if(!(newValue == null)){
                relatedTags.addAll(TagManager.search(newValue));
            }
            //sited from https://stackoverflow.com/questions/30465313/javafx-textfield-with-listener-gives-java-lang-illegalargumentexception-the-s
            Platform.runLater(() -> {
                newTag.getItems().clear();
                newTag.getItems().addAll(relatedTags);
            });
        });

        Button addTag = new Button("+");
        addTag.setOnAction(e -> {
            if(newTag.getValue() instanceof String){
                image.addTag((String)newTag.getValue());
            }
            //newTag.setValue("");
            addClickableTags();
            updateLog();
            imageNameUpdate();

        });

        layout.add(newTag, 6, 0, 1, 1);
        layout.add(addTag, 7, 0, 1, 1);

        return layout;

    }

    private void imageNameUpdate() {

        imageNames.getItems().removeAll(imageNames.getItems());

        for (Entry e : image.getLog()) {

            imageNames.getItems().add(e.getImageName());

        }

        if (!imageNames.getItems().isEmpty()) {
            Collections.reverse(imageNames.getItems());
            imageNames.setPromptText(imageNames.getItems().get(0));
        }
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
                imageNameUpdate();

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
