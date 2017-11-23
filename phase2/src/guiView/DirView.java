package guiView;

import guiController.*;
import ManageImage.ImageFile;
import ManageImage.ImageManager;
import guiController.Main;
import guiController.PicGrid;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DirView {
    /**
     * ImageFile for when changing the directory
     */
    private static ImageFile image;


    /**
     * Scene of the image selected
     */
    private static ImageScene imageScene;


    /**
     * Text that displays the directory of the ImageFile
     */
    private static Text directoryText;

    /**
     * Chosen directory by the user
     */
    private static File directory;

    // TODO write doc
//    private static DirController dirController = new DirController();


    /**
     * Show an alert if an image was unsuccessfully moved.
     * <p>
     * Common cause: similar image already exists in the same directory.
     */
    private static void imageMovingFailed(){
        // taken from http://code.makery.ch/blog/javafx-dialogs-official/
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Move Directory Error");
        alert.setHeaderText("Failed to move this image to " + directory.toString());
        alert.setContentText("The destination directory likely already contains another image named " + image.nameWithTags());
        alert.showAndWait();
    }



    /**
     * Sets the stage except for the goBtn.
     *
     * @param file True if a file was given when dirChooser is called
     * @param currentStage the Stage that the user is in
     */
    public static void guiSetup(Stage currentStage, boolean file) {
        Stage chooser = new Stage();

        chooser.setTitle("Image Viewer - Select Directory");

        GridPane pane = new GridPane();
        Scene scene = new Scene(pane, 500, 250);

        Button dirChooserBtn = new Button();
        dirChooserBtn.setText("...");

        Button goBtn = new Button();
        goBtn.setText("Go");

        TextField dirTextField = new TextField();
        dirTextField.setPrefWidth(350);
        dirTextField.setText("Input Directory");
        GridPane.setHalignment(dirTextField, HPos.LEFT);

        Text instruction = new Text();
        instruction.setText("Please select/type in a directory:");

        Text error = new Text();
        error.setText("Please select a working directory.");
        error.setFill(Color.RED);
        GridPane.setHalignment(error, HPos.RIGHT);
        error.setVisible(false);

        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(5, 5, 5, 5));
        pane.add(error, 4, 8);
        pane.add(dirTextField, 4, 7);
        pane.add(dirChooserBtn, 5, 7);
        pane.add(instruction, 4, 6);
        pane.add(goBtn, 5, 8);

        DirController.openDirectoryChooser(dirChooserBtn, dirTextField, error, currentStage);

        btmMethod(goBtn, file, currentStage, chooser, error, dirTextField);

        chooser.setMaximized(false);
        chooser.setScene(scene);
        //Cite: https://www.programcreek.com/java-api-examples/index.php?class=javafx.stage.Stage&method=showAndWait Date: Nov 5, 2017
        chooser.initModality(Modality.APPLICATION_MODAL);
        chooser.show();
    }

    /** // TODO: unsure about this method, talk to Allan about it
     * Sets the 'Go' button method according to if an ImageFile was passed when dirChooser is called.
     * If True, ImageFile will be moved to the selected directory.
     * If False, PicGrid will be shown.
     *
     * @param button  the 'Go' button
     * @param file True if a file was given when dirChooser is called
     * @param currentStage the Stage that the user is in
     * @param chooser  the Stage of the directory chooser
     * @param error the error message to be shown if user chose an invalid directory
     * @param dirTextField the TextField for showing the chosen directory
     */
    private static void btmMethod(Button button, boolean file, Stage currentStage, Stage chooser, Text error, TextField dirTextField) {
        button.setOnAction(
                e -> {
                    directory = new File(dirTextField.getText());
                    if (file) {
                        if (directory.isDirectory()) {

                            try {
                                boolean success = image.move(directory);
                                directoryText.setText(image.getDirectory().toString());
                                imageScene.updateLog();

                                if (!success) {
                                    DirView.imageMovingFailed();
                                }

                            } catch (IOException e1) {
                                Main.logger.log(Level.SEVERE, "new directory path not found.", e);
                            }
                            chooser.hide();
                        } else {
                            error.setVisible(true);
                        }
                    } else {
                        if (directory.isDirectory()) {
                            ImageManager.createImagesFromDirectory( directory.toString());

                            PicGrid p = new PicGrid(currentStage, directory);
                            p.picGrid();

                            currentStage.show();
                            chooser.hide();
                        } else {
                            error.setVisible(true);
                        }
                    }
                });
    }



    /**
     * Lets user to choose a directory and then moves the ImageFile to chosen directory.
     * <p>
     * Acts as an initiation for the GUI.
     *
     * @param currentStage the Stage that the user is in
     * @param imageToMove  the ImageFile to move
     * @param dirText      the Text to display the directory of the ImageFile
     */
    public static void dirChooser(Stage currentStage, ImageFile imageToMove, Text dirText, ImageScene is) {  // TODO: for moving an image file, probably the view
        imageScene = is;
        guiSetup(currentStage, true);
        image = imageToMove;
        directoryText = dirText;
    }
}
