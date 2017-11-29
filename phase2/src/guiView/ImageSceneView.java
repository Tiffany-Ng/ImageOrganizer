package guiView;

import guiController.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import ManageImage.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

/**
 * guiController of an individual image's information.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class ImageSceneView {

    /**
     * The image used.
     */
    private ImageFile image;

    /**
     * The actual scene which hold all the elements.
     */
    private Scene imageScene;

    /**
     * All the logs in a text box.
     */
    private static TextArea log;

    /**
     * Image's imageNewName
     */
    private TextField imageNewName;

    /**
     * Pane to hold clickable tags.
     */
    private FlowPane f;

    /**
     * The directory that the user first opened
     */
    private File directory;

    /**
     * The previous picGrid scene
     */
    private Stage prevScene;

    /**
     * All names the image has had.
     */
    private ComboBox<String> imageNames;

    /**
     * The hue of the customFilter
     */
    private Slider hue;

    /**
     * The contrast of the customFilter
     */
    private Slider contrast;

    /**
     * The brightness of the customFilter
     */
    private Slider brightness;

    /**
     * The saturation of the customFilter
     */
    private Slider saturation;

    /**
     * image in form of a viewable icon
     */
    private ImageView icon = new ImageView();

    /**
     * ArrayList for adding multiple tags
     */
    private ArrayList<String> tagsToAdd = new ArrayList<>();

    /**
     * ArrayList for deleting multiple tags
     */
    private ArrayList<String> tagsToDelete = new ArrayList<>();

    public static TextArea getLog() {
        return log;
    }

    ImageSceneView(Stage stage) {

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
        log = new TextArea();

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
        icon.setEffect(null);
        icon.setImage(image.getImage());
        icon.setPreserveRatio(true);

        // needs the "file://" because image will not understand it is a directory
        // solution found at https://stackoverflow.com/questions/8474694/java-url-unknown-protocol-c
        icon.setImage(image.getImage());
        layout.add(icon, 1, 2, 4, 2);

        // go to screen with all images
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        imageNames = new ComboBox<>();

        imageNewName = new TextField(image.getName());
        imageNewName.setEditable(true);
        imageSceneController.changeToNewName( imageNewName, log,imageNames);
        imageSceneController.setTagsToAdd(tagsToAdd);
        imageSceneController.setTagsToDelete(tagsToDelete);


        Button rename = new Button("Rename");
        imageSceneController.renameButtonClick(rename, imageNewName, imageNames, log);



        back.setOnAction(
                e -> SceneManager.swapToPicGrid(this.directory));

        // https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm

        imageSceneController.imageNameUpdate(imageNames, imageNewName);
        imageNames.setMaxWidth(380);
        imageNames.getSelectionModel().selectFirst();

        Button revertName = new Button("Revert");
        imageSceneController.revertOldTagName(revertName, imageNames, log, imageNewName);  // gives functionality to the Button


        VBox f = vBoxSetup();
        layout.add(f, 6, 2, 2, 2);

        // cite: https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm Nov 25, 2017
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Normal",
                        "Black and White",
                        "Invert",
                        "Custom"
                );
        ComboBox<String> comboBox = new ComboBox(options);
        comboBox.setValue("Normal");

        HBox customFilter = new HBox();
        Text hueText = new Text("Hue:");
        hue = new Slider(-1, 1, 0);
        hue.valueProperty().addListener(e -> {
            image.applyFilter(icon, brightness, contrast, hue, saturation);
        });
        customFilter.getChildren().addAll(hueText, hue);
        Text brightnessText = new Text("Brightness:");
        brightness = new Slider(-1, 1, 0);
        brightness.valueProperty().addListener(e -> {
            image.applyFilter(icon, brightness, contrast, hue, saturation);
        });
        customFilter.getChildren().addAll(brightnessText, brightness);
        Text saturationText = new Text("Saturation:");
        saturation = new Slider(-1, 1, 0);
        saturation.valueProperty().addListener(e -> {
            image.applyFilter(icon, brightness, contrast, hue, saturation);
        });
        customFilter.getChildren().addAll(saturationText, saturation);
        Text contrastText = new Text("Contrast:");
        contrast = new Slider(-1, 1, 0);
        contrast.valueProperty().addListener(e -> {
            image.applyFilter(icon, brightness, contrast, hue, saturation);
        });
        customFilter.getChildren().addAll(contrastText, contrast);
        customFilter.setVisible(false);

        //cite: https://stackoverflow.com/questions/41323945/javafx-combobox-add-listener-on-selected-item-value Nov 25, 2017
        comboBox.valueProperty().addListener((fields, oldValue, newValue) -> {
            if (newValue.equals("Custom")) {
                icon.setImage(image.getImage());
                customFilter.setVisible(true);
            } else {
                customFilter.setVisible(false);
            }
            image.setFilterStrategy(newValue);
            image.applyFilter(icon);
        });

        HBox imageName = new HBox();
        imageName.getChildren().addAll(imageNewName, rename, imageNames, revertName, comboBox);
        imageName.setSpacing(5.0);
        layout.add(customFilter, 1, 2, 1, 1);
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
        Text dirText = new Text();
        // Retrieved from: https://stackoverflow.com/questions/12737829/javafx-textfield-resize-to-text-length Date: Nov 21, 2017
        dirText.setWrappingWidth(280);
        dirText.setText(image.getDirectory().toString());

        // button for opening the directory
        Button openImgDir = new Button();
        openImgDir.setText("Open Directory");
        imageSceneController.openImageDirectory(openImgDir);

        // button for changing the directory
        Button changeDir = new Button();
        changeDir.setText("Change Directory");

        changeDir.setOnAction(
                e -> DirView.dirChooser(prevScene, this.image, dirText, this));

        HBox dir = new HBox();
        dir.boundsInParentProperty();
        dir.setMaxWidth(flow.getMaxWidth());
        dir.setSpacing(8.0);

        dir.getChildren().add(dirText);
        dir.getChildren().add(openImgDir);
        dir.getChildren().add(changeDir);
        changeDir.setAlignment(Pos.BASELINE_CENTER);

        // nested panes implemented from
        // https://stackoverflow.com/questions/33339427/javafx-have-multiple-panes-in-one-scene
        flow.getChildren().add(dir);
        flow.setAlignment(Pos.CENTER_LEFT);


        //https://docs.oracle.com/javafx/2/ui_controls/text-field.htm
        TextField newTag = new TextField();

        HBox tagBox = new HBox();
        Button addTag = new Button("+");
        imageSceneController.addTag(addTag, newTag, log,  imageNames, imageNewName);
        imageSceneController.setTagsToAdd(tagsToAdd);  // record the new tag which just got added



        tagBox.getChildren().addAll(newTag, addTag);
        tagBox.setSpacing(5.0);

        flow.getChildren().add(tagBox);

        Button updateTags = new Button("Update tags");
        imageSceneController.updateTags(updateTags, log, imageNames, imageNewName);

        tagBox.getChildren().add(updateTags);

        f = new FlowPane(Orientation.VERTICAL, 7, 5);
        imageSceneController.setFlowLayout(f);
        f.setPadding(new Insets(5));
        f.setPrefHeight(480 / 2.5);
        f = imageSceneController.addClickableTags();



        flow.getChildren().add(new ScrollPane(f));

        imageSceneController.updateLog(log);

        log.setWrapText(true);
        log.setEditable(false);

        // wrap error solution found at https://stackoverflow.com/questions/29537264/javafx-flowpane-autosize
        log.setPrefHeight(480 / 2);
        flow.getChildren().add(log);

        return flow;
    }


}
