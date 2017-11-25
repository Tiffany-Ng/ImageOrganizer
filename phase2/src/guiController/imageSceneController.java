package guiController;

import ManageImage.Entry;
import ManageImage.ImageFile;
import ManageImage.Log;
import ManageImage.TagManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;


import javax.swing.text.html.HTML;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class imageSceneController {

    private static FlowPane f;

    private static ImageFile image;

    public static void setF(FlowPane f) {
        imageSceneController.f = f;
    }

    public static void setImage(ImageFile image) {
        imageSceneController.image = image;
    }

    /**
     * Create a generic Alert using the information provided.
     *
     * @param title   String: window title
     * @param header  String
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
    public static void updateLog(TextArea log) {   // TODO: should be in the controller

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
    static public void imageNameUpdate(ComboBox<String> imageNames, TextField name) {  // TODO: not in the right package

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
    public static FlowPane addClickableTags(ComboBox<String> imageNames, TextArea log, TextField name, ArrayList<String> toAdd, ArrayList<String> toDelete) {  // TODO: change horrible name f

        f.getChildren().clear();
        //https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        LinkedList<String> tags = TagManager.getTags();
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            checkBoxes.add(checkBox);
            if(image.getTags().contains(tag))
                checkBox.setSelected(true);

            checkBox.setOnAction(e ->{
                if(checkBox.isSelected())
                    toAdd.add(checkBox.getText());
                else
                    toDelete.add(checkBox.getText());
            });
        }
        f.getChildren().addAll(checkBoxes);


        // taken from https://stackoverflow.com/questions/37378973/implement-tags-bar-in-javafx
//        List<String> tags = image.getTags();
//        f.getChildren().removeAll(f.getChildren());
//
//        ImageView i = new ImageView(new Image("file:///" + "x.jpeg"));
//
//        for (String t : tags) {
//
//            // makes all tags as clickable buttons
//            Button tag = new Button(t, i);
//
//            tag.setOnAction(
//                    e -> {
//                        boolean success = image.removeTag(tag.getText());
//                        if (success) {
//                            f.getChildren().remove(tag);
//                            imageSceneController.updateLog(log);
//                            imageNameUpdate(imageNames, name);
//                        } else {
//                            imageSceneController.createAlert("Remove Tag Error", "The tag '" + tag.getText() + "' was not removed successfully",
//                                    "Tag name contains ' @' or the file name without the tag is likely occupied");
//                        }
//                    });
//
//            f.getChildren().add(tag);
//       }
        return f;
    }


    /**
     * Adding the ability to revert to an older name on the guiController
     *
     * @param revertName Button that initiates action
     */
    public static void revertOldTagName(Button revertName, ComboBox<String> imageNames, TextArea log, TextField name, ArrayList<String> toAdd, ArrayList<String> toDelete) {    // TODO: should be in controller
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
                        updateLog(log);
                        addClickableTags(imageNames, log, name, toAdd, toDelete);
                        imageNameUpdate(imageNames, name);
                    }
                });
    }
}
