package guiController;

import ManageImage.ImageFile;
import guiView.DirView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * A stage that pops up when dirChooser is called.
 * There are two usages, pass in only a Stage to select a directory to open. It will then lead to PicGrid for a list of ImageFiles to show.
 * Pass in a Stage, an ImageFile, and a Text to select a directory for the ImageFile to transfer to. It will also update the Text to show
 * the new directory.
 * Functionality of panes from: https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm Date: Nov 6, 2017
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class DirController {



    /**
     * Lets user to choose a directory and then shows PicGrid.
     *
     * @param currentStage the Stage that the user is in
     */
    static void dirChooser(Stage currentStage) {
        DirView.guiSetup(currentStage, false);
    }

    /**
     * Check if the user has decided to open a directory by clicking on a button
     *
     * @param dirChooserButton Button that user clicks for choosing a directory
     * @param dirTextField Displays the path of the selected directory
     * @param error Error message in case an improper directory is provided
     * @param currentStage Representation in which all actions will be taking place
     */
    public static void openDirectoryChooser(Button dirChooserButton, TextField dirTextField, Text error, Stage currentStage){
        dirChooserButton.setOnAction(
                e -> {
                    // Adapted from: https://docs.oracle.com/javafx/2/ui_controls/file-chooser.html Date: Nov 5, 2017
                    DirectoryChooser dirChooser = new DirectoryChooser();
                    File directory = dirChooser.showDialog(currentStage);
                    if (directory != null) {
                        dirTextField.setText(directory.toString());
                        error.setVisible(false);
                    }
                });
    }



}
