package GUI;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

import ManageImage.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.io.File;

/**
 * GUI of an individual image's information
 */
public class ImageScene {

    /**
     * Combo box to hold previously used tags
     */
    ComboBox newTag;

    /** The image used . */
    private ImageFile image;

    /** The actual scene with hold all the elements. */
    private Scene imageScene;

    /** Main pane of the scene. */
    private GridPane g;

    /** All the logs in a text box. */
    private TextArea log;

    /** Pane to hold clickable tags. */
    private FlowPane f;

    /** The directory that the user first opened */
    private File directory;

    /** The previous picGrid scene */
    private Stage prevScene;

    /** All names the image has had. */
    private ComboBox<String> imageNames;

    /**
     * Construct an GUI.ImageScene.
     *
     * @param image
     */
    public ImageScene(ImageFile image, File directory, Stage prevScene) throws IOException {

        this.image = image;

        // inspired from https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        g = gridSetup();
        imageScene = new Scene(g);

        this.directory = directory;
        this.prevScene = prevScene;

        prevScene.setMaximized(false);
        // prevScene.sizeToScene();
        prevScene.setWidth(1325);
        prevScene.setHeight(750);

        // Source: https://stackoverflow.com/questions/13702191/center-location-of-stage (Nov 16, 2017)
        prevScene.setX(200);
        prevScene.setY(100);

        prevScene.setResizable(false);
    }

    /**
     * Setup of the whole screen
     *
     * @return GridPane
     */
    private GridPane gridSetup() throws IOException {

        GridPane layout = new GridPane();
        layout.setHgap(6);
        layout.setVgap(6);
        layout.setPadding(new Insets(10, 10, 10, 10));

        // image in form of a viewable icon
        ImageView icon = new ImageView();

        // https://stackoverflow.com/questions/27894945/how-do-i-resize-an-imageview-image-in-javafx
        icon.setFitWidth(720);
        icon.setFitHeight(480);
        icon.setPreserveRatio(true);

        // needs the "file://" because image will not understand it is a directory
        // https://stackoverflow.com/questions/8474694/java-url-unknown-protocol-c
        icon.setImage(new Image("file:///" + image.getFile().toString()));

        // flowPane for image information
        VBox f = vBoxSetup();

        //    HBox h = new HBox();
        //    h.getChildren().add(icon);
        //    h.getChildren().add(f);
        //    h.setSpacing(5);

        layout.add(icon, 1, 2, 4, 2);
        layout.add(f, 6, 2, 2, 2);

        // go to main screen
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        TextField name = new TextField(image.getName());
        name.setEditable(true);

        // https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2
        name.setOnKeyPressed(k -> {

            if (k.getCode().equals(KeyCode.ENTER)) {

                if (!image.rename(name.getText())) {

                    // http://code.makery.ch/blog/javafx-dialogs-official/
                    Alert badName = new Alert(Alert.AlertType.ERROR);
                    badName.setTitle("Invalid Name");
                    badName.setHeaderText("The name you entered is invalid.");
                    badName.setContentText("Make sure there are no '@' symbols in your name.");
                    badName.showAndWait();

                }

                else {

                    updateLog();
                    imageNameUpdate();

                }

            }

        });

        back.setOnAction(
                e -> {
                    new PicGrid(prevScene, this.directory).picGrid();
                });

        // https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm
        imageNames = new ComboBox<>();
        imageNameUpdate();
        imageNames.setMaxWidth(590);
        imageNames.getSelectionModel().selectFirst();

        Button revertName = new Button("Revert");
        revertName.setOnAction(
                event -> {
                    if (!imageNames.getSelectionModel().isEmpty()
                            && !image.nameWithTags().equals(imageNames.getValue())) {

                        ArrayList<String> allNames = new ArrayList<>(imageNames.getItems());

                        // https://stackoverflow.com/questions/14987971/added-elements-in-arraylist-in-the-reverse-order-in-java
                        Collections.reverse(allNames);

                        image.revertName(allNames.indexOf(imageNames.getValue()));
                        updateLog();
                        addClickableTags();
                        imageNameUpdate();
                        name.setText(image.getName());

                    }
                });

        HBox imageName = new HBox();
        imageName.getChildren().addAll(name, imageNames, revertName);
        imageName.setSpacing(5.0);

        layout.add(imageName, 1, 0, 1, 1);

        layout.setPrefHeight(700);
        layout.setPrefWidth(1325);
        return layout;
    }

    /** Update the comboBox of image names. */
    private void imageNameUpdate() {

        imageNames.getItems().removeAll(imageNames.getItems());

        for (String name : image.getPriorNames()) {

            imageNames.getItems().add(name);
        }

        if (!imageNames.getItems().isEmpty()) {
            Collections.reverse(imageNames.getItems());
            imageNames.setPromptText(imageNames.getItems().get(0));
        }
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


        // button for opening the directory
        Button openDir = new Button();
        openDir.setText("Open Directory");

      openDir.setOnAction(
              e -> {
                try {
                  Desktop.getDesktop().open(image.getDirectory());
                } catch (IOException ex) {
                  Main.logger.warning("Can't open directory");
                }
              });

        // button for changing the directory
        Button changeDir = new Button();
        changeDir.setText("Change Directory");

        changeDir.setOnAction(
                e -> {
                    DirChooser.dirChooser(prevScene, this.image, directory);
                });

        HBox dir = new HBox();
        dir.boundsInParentProperty();
        dir.setMaxWidth(flow.getMaxWidth());
        dir.setSpacing(5.0);

        dir.getChildren().add(directory);
        dir.getChildren().add(openDir);
        dir.getChildren().add(changeDir);
        changeDir.setAlignment(Pos.CENTER);

        // nested panes implemented from
        // https://stackoverflow.com/questions/33339427/javafx-have-multiple-panes-in-one-scene
        flow.getChildren().add(dir);
        flow.setAlignment(Pos.CENTER_RIGHT);

        // flow.getChildren().add(changeDir);

        ComboBox newTag = new ComboBox();
        newTag.setEditable(true);
        newTag.getItems().addAll(TagManager.tags);

        newTag
                .getEditor()
                .setOnMouseClicked(
                        e -> {
                            newTag.show();
                            newTag.setVisibleRowCount(10);
                        });

//            newTag.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//              if (event.getCode() == KeyCode.ENTER) {
//                  System.out.println("kdjla");
//                  if (newTag.getValue() instanceof String) {
//                      image.addTag((String) newTag.getValue());
//                      newTag.setValue("");
//                      newTag.hide();
//                  }
//                  // newTag.setValue("");
//                  addClickableTags();
//                  updateLog();
//                  imageNameUpdate();
//              }
//            });

        newTag
                .getEditor()
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            LinkedList<String> relatedTags = new LinkedList<>();

                            /*Platform.runLater(
                                    () -> {
                                        newTag.getItems().clear();
                                        if (newValue.length() == 0) {
                                            newTag.getItems().addAll(TagManager.tags);
                                            newTag.setVisibleRowCount(10);
                                        } else {
                                            relatedTags.addAll(TagManager.search(newValue));
                                            newTag.setVisibleRowCount(relatedTags.size());
                                            newTag.getItems().addAll(relatedTags);
                                        }

                                    });
                            newTag.getEditor().setText(newValue);
                            */

                        });

        HBox tagBox = new HBox();
        Button addTag = new Button("+");
        addTag.setOnAction(
                e -> {
                    if (newTag.getValue() instanceof String && ((String) newTag.getValue()).length() != 0) {
                        image.addTag((String) newTag.getValue());
                        //newTag.setValue("");
                    }

                    addClickableTags();
                    updateLog();
                    imageNameUpdate();

                });

        Text instruction = new Text();
        instruction.setText("Wanna delete a tag?,  SELECT IT!");
        // instruction.setFont(Font.font(java.awt.Font.SERIF, 16));
        // instruction.setFill(Color.DARKBLUE);


        tagBox.getChildren().addAll(newTag, addTag, instruction);
        tagBox.setSpacing(5.0);

        flow.getChildren().add(tagBox);

        // make all the tags
        // sub-flowPane to hold the tags
        f = new FlowPane(Orientation.HORIZONTAL, 7, 5);
        f.setPadding(new Insets(5));
        f.setPrefHeight(480 / 2.5);
        f = addClickableTags();

        flow.getChildren().add(new ScrollPane(f));

        log = new TextArea();
        // image log
        updateLog();

        log.setWrapText(true);
        log.setEditable(false);

        // wrap error solution https://stackoverflow.com/questions/29537264/javafx-flowpane-autosize
        log.setPrefHeight(480 / 2);

        flow.getChildren().add(log);

        return flow;
    }

    /**
     * Add all tags image has into a clickable section of the scene.
     *
     * @return FlowPane
     */
    private FlowPane addClickableTags() {

        // https://stackoverflow.com/questions/37378973/implement-tags-bar-in-javafx
        List<String> tags = image.getTags();
        f.getChildren().removeAll(f.getChildren());

        ImageView i = new ImageView(new Image("file:///" + "x.jpeg"));

        for (String t : tags) {

            // makes all tags as clickable buttons
            Button tag = new Button(t, i);

            tag.setOnAction(
                    e -> {
                        image.removeTag(tag.getText());
                        f.getChildren().remove(tag);
                        updateLog();
                        imageNameUpdate();
                    });

            f.getChildren().add(tag);
        }

        return f;
    }

    /** Update the log with its new information. */
    private void updateLog() {

        Log imageLog = image.getLog();
        StringBuilder logs = new StringBuilder();

        // add all logs as a line
        for (Entry e : imageLog) {

            // https://stackoverflow.com/questions/15977295/control-for-displaying-multiline-text
            logs.append(e.toString()).append(System.lineSeparator());
        }

        // represent log as textArea
        log.setText(logs.toString());
    }

    public Scene getImageScene() {
        return imageScene;
    }
}
