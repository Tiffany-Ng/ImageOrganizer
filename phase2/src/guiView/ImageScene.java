package guiView;

import guiController.*;
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
 * guiController of an individual image's information.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class ImageScene {

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
     * FilterStrategy for  applying filter to images
     */
    private static FilterStrategy strategy;

    /**
     * image in form of a viewable icon
     */
    private ImageView icon = new ImageView();

    public ImageScene(Stage stage) {

        this.prevScene = stage;

    }

    /**
     * Construct the guiController view of an image
     *
     * @param image     particular image in a directory
     * @param directory Location of the file containing images
     * @throws IOException Case when invalid directory
     */
    public void initialize(ImageFile image, File directory) throws IOException {

        this.image = image;


        // inspired from https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        GridPane g;
        imageSceneController.setImage(image);
        g = gridSetup();
        imageScene = new Scene(g);
        this.directory = directory;

        // cite: https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java
        // Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        // prevScene.setX((screenSize.getWidth() - prevScene.getWidth()) / 2);
        // prevScene.setY((screenSize.getHeight() - prevScene.getHeight()) / 2);
        // prevScene.setResizable(false);
    }

    /**
     * cite : https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2
     * rename the image to the name collected from the relevant texBox(on hitting enter)
     */
    private void renameImageFile() {

        boolean success = image.rename(name.getText());
        imageSceneController.addClickableTags( imageNames, log, name);
        imageSceneController.updateLog(log);
        imageSceneController.imageNameUpdate(imageNames, name);

        if (!success) {

            imageSceneController.createAlert("Invalid Name", "The name you entered is invalid.",
                    "A name should not contain ' @' and the name " + name.getText() + " must be available");
        }

    }


    /**
     * Check if the string entered by the user on the guiController is a valid tag
     *
     * @param newTag field which contains the new tag name
     * @return true if valid tag name.
     */
    private boolean checkValidTagName(ComboBox<String> newTag) {
        return newTag.getValue() != null && (newTag.getValue()).length() != 0;
    }


    /**  // TODO: COPY  ... Kept this because of DirVies's call
     * Update the log with its new information.
     */
    public void updateLog() {   // TODO: should be in the controller

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
     * Setup of the whole screen combining all smaller elements and layouts.
     *
     * @return GridPane
     */
    private GridPane gridSetup() throws IOException {

        GridPane layout = new GridPane();
        layout.setHgap(6);
        layout.setVgap(6);
        layout.setPadding(new Insets(10, 10, 10, 10));

        // ratio preserve solution
        // https://stackoverflow.com/questions/27894945/how-do-i-resize-an-imageview-image-in-javafx
        icon.setFitWidth(720);
        icon.setFitHeight(480);
        icon.setPreserveRatio(true);

        // needs the "file://" because image will not understand it is a directory
        // solution found at https://stackoverflow.com/questions/8474694/java-url-unknown-protocol-c
        icon.setImage(new Image("file:///" + image.getFile().toString()));
        layout.add(icon, 1, 2, 4, 2);

        // go to screen with all images
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        name = new TextField(image.getName());
        name.setEditable(true);
        name.setOnKeyPressed(   // TODO: should be in the controller
                k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        this.renameImageFile();
                    }
                });

        Button rename = new Button("Rename");
        rename.setOnAction(   // Todo; should be in controller
                e -> this.renameImageFile());


        back.setOnAction(     // Todo: should be in controller
                e -> SceneManager.swapToPicGrid(this.directory)); //new PicGrid(prevScene, this.directory).picGrid());

        // https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm
        imageNames = new ComboBox<>();
        imageSceneController.imageNameUpdate(imageNames, name);
        imageNames.setMaxWidth(380);
        imageNames.getSelectionModel().selectFirst();

        Button revertName = new Button("Revert");
        imageSceneController.revertOldTagName( revertName, imageNames, log,  name);  // gives functionality to the Button


        VBox f = vBoxSetup();  // TODO: moved HERE
        layout.add(f, 6, 2, 2, 2);

        Button filter = new Button("Filter");   // TODo should be in controller
        filter.setOnAction(
                e -> {
                    setFilterStrategy(new InvertColoursFilter());
                    icon = applyFilter(icon);
                }
        );

        HBox imageName = new HBox();
        imageName.getChildren().addAll(name, rename, imageNames, revertName, filter);
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
        // Retrieved from: https://stackoverflow.com/questions/12737829/javafx-textfield-resize-to-text-length Date: Nov 21, 2017
        directory.setWrappingWidth(280);
        directory.setText(image.getDirectory().toString());

        // button for opening the directory
        Button openDir = new Button();
        openDir.setText("Open Directory");
        openDir.setOnAction(   // TODO: dirController maybe?
                e -> {
                    try {

                        //Adapted from: https://stackoverflow.com/questions/7357969/how-to-use-java-code-to-open-windows-file-explorer-and-highlight-the-specified-f Date: Nov 19, 2017
                        Runtime.getRuntime().exec("explorer.exe /select," + image.getFile().getAbsolutePath());

                    } catch (IOException ex) {
                        imageSceneController.createAlert("Open directory error", "Execution failed",
                                "The execution is not supported on this computer");
                        Main.logger.log(Level.SEVERE, "Can't open directory", ex);
                    }
                });


        // button for changing the directory
        Button changeDir = new Button();
        changeDir.setText("Change Directory");

        changeDir.setOnAction(
                e -> DirView.dirChooser(prevScene, this.image, directory, this));

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
        addTag.setOnAction(   // TODO: in controller
                e -> {
                    if (checkValidTagName(newTag)) {
                        boolean success = image.addTag(newTag.getValue());
                        if (success) {
                            newTag.setValue("");
                        } else {
                            imageSceneController.createAlert("Add Tag Error", "The tag '" + newTag.getValue() + "' was not added successfully",
                                    "Tag name contains ' @', the tag already exists, or the file name with the additional tag is occupied");
                        }
                    }

                    imageSceneController.addClickableTags(imageNames,  log, name);
                    imageSceneController.updateLog(log);
                    imageSceneController.imageNameUpdate(imageNames, name);
                });

        Button addToAll = new Button("Add to All");
        addToAll.setOnAction(   // TODO: in controller
                e -> {
                    if (checkValidTagName(newTag)) {
                        ImageManager.addGlobalTag(newTag.getValue());
                        newTag.setValue("");
                    }
                    SceneManager.swapToPicGrid(this.directory);

                });

        Button deleteFromAll = new Button("Delete from All");
        deleteFromAll.setOnAction(   // TODO: in controller
                e -> {
                    if (checkValidTagName(newTag)) {
                        ImageManager.deleteGlobalTag(newTag.getValue());
                        newTag.setValue("");
                    }
                    SceneManager.swapToPicGrid(this.directory);
                });

        tagBox.getChildren().addAll(newTag, addTag, addToAll, deleteFromAll);
        tagBox.setSpacing(5.0);

        flow.getChildren().add(tagBox);

        Text instruction = new Text();
        instruction.setText("Want to delete a tag? SELECT IT!");


        flow.getChildren().add(instruction);

        f = new FlowPane(Orientation.HORIZONTAL, 7, 5);
        imageSceneController.setF(f);
        f.setPadding(new Insets(5));
        f.setPrefHeight(480 / 2.5);
        f = imageSceneController.addClickableTags(imageNames, log, name);

        flow.getChildren().add(new ScrollPane(f));

        log = new TextArea();

        // image log
        imageSceneController.updateLog(log);

        log.setWrapText(true);
        log.setEditable(false);

        // wrap error solution found at https://stackoverflow.com/questions/29537264/javafx-flowpane-autosize
        log.setPrefHeight(480 / 2);
        flow.getChildren().add(log);

        return flow;
    }


    /**
     * Sets the FilterStrategy
     *
     * @param chosenStrategy the FilterStrategy that is being used, could be different kinds of filter.
     */
    private void setFilterStrategy(FilterStrategy chosenStrategy) {
        strategy = chosenStrategy;
    }

    /**
     * Returns the chosen directory by the user
     *
     * @return ImageView the ImageView after filter has been applied to it
     */
    private static ImageView applyFilter(ImageView imageView) {
        return strategy.applyFilter(imageView);
    }

}
