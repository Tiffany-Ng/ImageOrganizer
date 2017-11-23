package guiController;

import ManageImage.Entry;
import ManageImage.ImageFile;
import ManageImage.Log;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class imageSceneController {


    /**
     * Create a generic Alert using the information provided.
     *
     * @param title String: window title
     * @param header String
     * @param content String
     */
    public static void createAlert(String title, String header, String content) {

        // taken from http://code.makery.ch/blog/javafx-dialogs-official/
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Update the log with its new information.
     */
    public static void updateLog(ImageFile image, TextArea log) {   // TODO: should be in the controller

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
     * Update the comboBox of image names and the text box name.
     */
    static public void imageNameUpdate(ComboBox<String> imageNames, ImageFile image,TextField name) {  // TODO: not in the right package

        name.setText(image.getName());
        imageNames.getItems().clear();

        for (String n : image.getPriorNames()) {
            imageNames.getItems().add(n);
        }
        if (!imageNames.getItems().isEmpty()) {
            Collections.reverse(imageNames.getItems());
            imageNames.getSelectionModel().selectFirst();
        }
    }


    /**
     * Add all tags image has into a clickable section of the scene.
     *
     * @return FlowPane
     */
    public static FlowPane addClickableTags(ImageFile image, ComboBox<String> imageNames, FlowPane f, TextArea log, TextField name) {  // TODO: change horrible name f

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
                            imageSceneController.updateLog(image, log);
                            imageNameUpdate(imageNames, image, name);
                        } else {
                            imageSceneController.createAlert("Remove Tag Error", "The tag '" + tag.getText() + "' was not removed successfully",
                                    "Tag name contains ' @' or the file name without the tag is likely occupied");
                        }
                    });

            f.getChildren().add(tag);
        }
        return f;
    }


    /**
     * Adding the ability to revert to an older name on the guiController
     *
     * @param revertName Button that initiates action
     */
    public static void revertOldTagName(Button revertName, ComboBox<String> imageNames, ImageFile image, TextArea log, FlowPane f, TextField name) {    // TODO: should be in controller
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
                        updateLog(image, log);
                        addClickableTags( image, imageNames, f, log, name);
                        imageNameUpdate(imageNames, image, name);
                    }
                });
    }
}
