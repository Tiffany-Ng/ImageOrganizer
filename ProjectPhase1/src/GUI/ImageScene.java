package GUI;

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
import javafx.scene.layout.*;

import ManageImage.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.util.logging.Level;

/**
 * GUI of an individual image's information.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
class ImageScene {

    /** The image used. */
    private ImageFile image;

    /** The actual scene which hold all the elements.*/
    private Scene imageScene;

    /** All the logs in a text box.*/
    private TextArea log;

    /** Image's name*/
    private TextField name;

    /** Pane to hold clickable tags. */
    private FlowPane f;

    /** The directory that the user first opened */
    private File directory;

    /** The previous picGrid scene */
    private Stage prevScene;

    /** All names the image has had. */
    private ComboBox<String> imageNames;

    /**
     * Construct the GUI view of an image
     *
     * @param image     particular image in a directory
     * @param directory Location of the file containing images
     * @param prevScene Reference to GUI with all images in a directory
     * @throws IOException Case when invalid directory
     */
    ImageScene(ImageFile image, File directory, Stage prevScene) throws IOException {

        this.image = image;

        // inspired from https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        GridPane g;
        g = gridSetup();
        imageScene = new Scene(g);

        this.directory = directory;
        this.prevScene = prevScene;

        prevScene.setMaximized(false);
        prevScene.setWidth(1325);
        prevScene.setHeight(750);

        // cite: https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java
        Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        prevScene.setX((screenSize.getWidth() - prevScene.getWidth()) / 2);
        prevScene.setY((screenSize.getHeight() - prevScene.getHeight()) / 2);
        prevScene.setResizable(false);
    }

    /**
     * cite : https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2
     * rename the image to the name collected from the relevant texBox(on hitting enter)
     */
    private void renameImageFile() {

        boolean success = image.rename(name.getText());
        addClickableTags();
        updateLog();
        imageNameUpdate();

        if (!success) {

            createAlert("Invalid Name", "The name you entered is invalid.",
                    "A name should not contain ' @' and the name " + name.getText() + " must be available");
        }

    }

    /**
     * Create a generic Alert using the information provided.
     *
     * @param title String: window title
     * @param header String
     * @param content String
     */
    private void createAlert(String title, String header, String content) {

        // taken from http://code.makery.ch/blog/javafx-dialogs-official/
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Adding the ability to revert to an older name on the GUI
     *
     * @param revertName Button that initiates action
     */
    private void revertOldTagName(Button revertName) {
        revertName.setOnAction(
                event -> {
                    if (!imageNames.getSelectionModel().isEmpty()
                            && !image.nameWithTags().equals(imageNames.getValue())) {

                        ArrayList<String> allNames = new ArrayList<>(imageNames.getItems());

                        //Cite: https://stackoverflow.com/questions/14987971/added-elements-in-arraylist-in-the-reverse-order-in-java
                        Collections.reverse(allNames);

                        boolean success = image.revertName(allNames.indexOf(imageNames.getValue()));
                        if (!success) {
                            createAlert("Revert Error", "The revert is invalid",
                                    "The file name " + imageNames.getValue() + " must be available");
                        }
                        updateLog();
                        addClickableTags();
                        imageNameUpdate();
                    }
                });
    }

    /**
     * Check if the string entered by the user on the GUI is a valid tag
     *
     * @param newTag field which contains the new tag name
     * @return true if valid tag name.
     */
    private boolean checkValidTagName(ComboBox<String> newTag) {
        return newTag.getValue() != null && (newTag.getValue()).length() != 0;
    }


    /**
     * Update the comboBox of image names and the text box name.
     */
    private void imageNameUpdate() {

        name.setText(image.getName());
        imageNames.getItems().clear();

        for (String name : image.getPriorNames()) {
            imageNames.getItems().add(name);
        }
        if (!imageNames.getItems().isEmpty()) {
            Collections.reverse(imageNames.getItems());
            imageNames.getSelectionModel().selectFirst();
        }
    }

    /**
     * Update the log with its new information.
     */
    void updateLog() {

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

    /**
     * * Return the scene which holds the elements.
     *
     * @return current interaction's screen
     */
    Scene getImageScene() {
        return imageScene;
    }

    /**
     * Add all tags image has into a clickable section of the scene.
     *
     * @return FlowPane
     */
    private FlowPane addClickableTags() {

        // taken from https://stackoverflow.com/questions/37378973/implement-tags-bar-in-javafx
        List<String> tags = image.getTags();
        f.getChildren().removeAll(f.getChildren());

        ImageView i = new ImageView(new Image("file:///" + "x.jpeg"));

        for (String t : tags) {

            // makes all tags as clickable buttons
            Button tag = new Button(t, i);

            tag.setOnAction(
                    e -> {
                        boolean success = image.removeTag(tag.getText());
                        if (success) {
                            f.getChildren().remove(tag);
                            updateLog();
                            imageNameUpdate();
                        } else {
                            createAlert("Remove Tag Error", "The tag '" + tag.getText() + "' was not removed successfully",
                                    "Tag name contains ' @' or the file name without the tag is likely occupied");
                        }
                    });

            f.getChildren().add(tag);
        }
        return f;
    }


    /**
     * Setup of the whole screen combining all smaller elements and layouts.
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

        // ratio preserve solution
        // https://stackoverflow.com/questions/27894945/how-do-i-resize-an-imageview-image-in-javafx
        icon.setFitWidth(720);
        icon.setFitHeight(480);
        icon.setPreserveRatio(true);

        // needs the "file://" because image will not understand it is a directory
        // solution found at https://stackoverflow.com/questions/8474694/java-url-unknown-protocol-c
        icon.setImage(new Image("file:///" + image.getFile().toString()));
        layout.add(icon, 1, 2, 4, 2);

        // flowPane for image information
        VBox f = vBoxSetup();
        layout.add(f, 6, 2, 2, 2);

        // go to screen with all images
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        name = new TextField(image.getName());
        name.setEditable(true);
        name.setOnKeyPressed(
                k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        this.renameImageFile();
                    }
                });

        Button rename = new Button("Rename");
        rename.setOnAction(
                e -> this.renameImageFile());


        back.setOnAction(
                e -> new PicGrid(prevScene, this.directory).picGrid());

        // https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm
        imageNames = new ComboBox<>();
        imageNameUpdate();
        imageNames.setMaxWidth(550);
        imageNames.getSelectionModel().selectFirst();

        Button revertName = new Button("Revert");
        this.revertOldTagName(revertName);  // gives functionality to the Button

        HBox imageName = new HBox();
        imageName.getChildren().addAll(name, rename, imageNames, revertName);
        imageName.setSpacing(5.0);
        layout.add(imageName, 1, 0, 1, 1);

        layout.setPrefHeight(700);
        layout.setPrefWidth(1325);
        return layout;
    }


    /**
     * Setup the image's information (log, tags, directory) in a flowPane.
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

                      //Adapted from: https://stackoverflow.com/questions/7357969/how-to-use-java-code-to-open-windows-file-explorer-and-highlight-the-specified-f Date: Nov 19, 2017
                      Runtime.getRuntime().exec("explorer.exe /select," + image.getFile().getAbsolutePath());

                    } catch (IOException ex) {
                      createAlert("Open directory error", "Execution failed",
                              "The execution is not supported on this computer");
                        Main.logger.log(Level.SEVERE, "Can't open directory", ex);
                    }
                });
        

        // button for changing the directory
        Button changeDir = new Button();
        changeDir.setText("Change Directory");

        changeDir.setOnAction(
                e -> DirChooser.dirChooser(prevScene, this.image, directory, this));

        HBox dir = new HBox();
        dir.boundsInParentProperty();
        dir.setMaxWidth(flow.getMaxWidth());
        dir.setSpacing(8.0);

        dir.getChildren().add(directory);
        dir.getChildren().add(openDir);
        dir.getChildren().add(changeDir);
        changeDir.setAlignment(Pos.BASELINE_CENTER);

        // nested panes implemented from
        // https://stackoverflow.com/questions/33339427/javafx-have-multiple-panes-in-one-scene
        flow.getChildren().add(dir);
        flow.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> newTag = new ComboBox<>();
        newTag.setEditable(true);
        newTag.getItems().addAll(TagManager.getTags());

        newTag
                .getEditor()
                .setOnMouseClicked(
                        e -> {
                            newTag.show();
                            newTag.setVisibleRowCount(10);
                        });

        HBox tagBox = new HBox();
        Button addTag = new Button("+");
        addTag.setOnAction(
                e -> {
                    if (checkValidTagName(newTag)) {
                        boolean success = image.addTag(newTag.getValue());
                        if (success) {
                            newTag.setValue("");
                        } else {
                            createAlert("Add Tag Error", "The tag '" + newTag.getValue() + "' was not added successfully",
                                    "Tag name contains ' @', the tag already exists, or the file name with the additional tag is occupied");
                        }
                    }

                    addClickableTags();
                    updateLog();
                    imageNameUpdate();
                });

        Button addToAll = new Button("Add to All");
        addToAll.setOnAction(
                e -> {
                    if (checkValidTagName(newTag)) {
                        ImageManager.addGlobalTag(newTag.getValue());
                        newTag.setValue("");
                    }
                    new PicGrid(prevScene, this.directory).picGrid();

                });

        Button deleteFromAll = new Button("Delete from All");
        deleteFromAll.setOnAction(
                e -> {
                    if (checkValidTagName(newTag)) {
                        ImageManager.deleteGlobalTag(newTag.getValue());
                        newTag.setValue("");
                    }
                    new PicGrid(prevScene, this.directory).picGrid();
                });

        tagBox.getChildren().addAll(newTag, addTag, addToAll, deleteFromAll);
        tagBox.setSpacing(5.0);

        flow.getChildren().add(tagBox);

        Text instruction = new Text();
        instruction.setText("Want to delete a tag? SELECT IT!");


        flow.getChildren().add(instruction);

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

        // wrap error solution found at https://stackoverflow.com/questions/29537264/javafx-flowpane-autosize
        log.setPrefHeight(480 / 2);
        flow.getChildren().add(log);

        return flow;
    }


}
