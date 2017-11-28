package guiController;

import ManageImage.*;
import guiView.Main;
import guiView.SceneManager;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;

public class imageSceneController {

    private static FlowPane flowLayout;

    private static ImageFile image;

    private static File directory;

    public static void setDirectory(File directory) {
        imageSceneController.directory = directory;
    }

    public static void setFlowLayout(FlowPane flowLayout) {
        imageSceneController.flowLayout = flowLayout;
    }

    public static void setImage(ImageFile image) {
        imageSceneController.image = image;
    }

    /**
     * Check if the string entered by the user on the guiController is a valid tag
     *
     * @param newTag field which contains the new tag imageNewName
     * @return true if valid tag imageNewName.
     */
    private static boolean checkValidTagName(String newTag) {
        return newTag != null && newTag.length() != 0;
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
    public static FlowPane addClickableTags(ArrayList<String> toAdd, ArrayList<String> toDelete) {  // TODO: change horrible name flowLayout

        flowLayout.getChildren().clear();
        //https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        LinkedList<String> tags = TagManager.getTags();
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            checkBoxes.add(checkBox);
            if(image.getTags().contains(tag))
                checkBox.setSelected(true);

            checkBox.setOnAction(e ->{
                String text = checkBox.getText();
                if(checkBox.isSelected()) {
                    toAdd.add(text);
                    toDelete.remove(text);
                }
                else {
                    toDelete.add(text);
                    toAdd.remove(text);
                }
            });
        }
        flowLayout.getChildren().addAll(checkBoxes);
        return flowLayout;
    }


    /**
     * Adding the ability to revert to an older name on the guiController
     *
     * @param revertName Button that initiates action
     */
    public static void revertOldTagName(Button revertName, ComboBox<String> imageNames, TextArea log, TextField name, ArrayList<String> toAdd, ArrayList<String> toDelete) {
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
                        addClickableTags( toAdd, toDelete);
                        imageNameUpdate(imageNames, name);
                    }
                });
    }


    /**
     * cite : https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2
     * rename the image to the name collected from the relevant texBox(on hitting enter)
     */
    public static void renameImageFile(TextField name, ArrayList<String> tagsToAdd, ArrayList<String> tagsToDelete, TextArea log, ComboBox imageNames) {

        boolean success = image.rename(name.getText());
        imageSceneController.addClickableTags(tagsToAdd, tagsToDelete);
        imageSceneController.updateLog(log);
        imageSceneController.imageNameUpdate(imageNames, name);

        if (!success) {

            imageSceneController.createAlert("Invalid Name", "The name you entered is invalid.",
                    "A name should not contain ' @' and the name " + name.getText() + " must be available");
        }
    }


    public static void changeToNewName(TextField imageNewName, ArrayList<String> tagsToAdd, ArrayList<String> tagsToDelete, TextArea log, ComboBox imageNames){
        imageNewName.setOnKeyPressed(   // TODO: should be in the controller
                k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        imageSceneController.renameImageFile(imageNewName, tagsToAdd, tagsToDelete, log,imageNames);
                    }
                });
    }

    public static void renameButtonClick(Button rename, TextField imageNewName, ArrayList<String> tagsToAdd, ArrayList<String> tagsToDelete, ComboBox imageNames, TextArea log){
     rename.setOnAction(
                e -> imageSceneController.renameImageFile(imageNewName, tagsToAdd, tagsToDelete, log,imageNames));
    }

    public static void openImageDirectory(Button openNewDir){
        openNewDir.setOnAction(
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
    }

    public static void addTag(Button addTag, TextField newTag, ArrayList<String> tagsToAdd, ArrayList<String> tagsToDelete, TextArea log, ComboBox<String> imageNames, TextField imageNewName){
        addTag.setOnAction(   // TODO: in controller
                e -> {
                    if (checkValidTagName(newTag.getText())) {
                        boolean success = image.addTag(newTag.getText());
                        if (success) {
                            newTag.setText("");
                        } else {
                            imageSceneController.createAlert("Add Tag Error", "The tag '" + newTag.getText() + "' was not added successfully",
                                    "Tag imageNewName contains ' @', the tag already exists, or the file imageNewName with the additional tag is occupied");
                        }
                    }

                    imageSceneController.addClickableTags(tagsToAdd, tagsToDelete);
                    imageSceneController.updateLog(log);
                    imageSceneController.imageNameUpdate(imageNames, imageNewName);
                });
    }


    public static void addTagAllImages(Button addToAll, TextField newTag){
        addToAll.setOnAction(
                e -> {
                    if (checkValidTagName(newTag.getText())) {
                        ImageManager.addGlobalTag(newTag.getText());
                        newTag.setText("");
                    }
                    SceneManager.swapToPicGrid(directory);

                });
    }


    public static void deleteTagAllImages(Button deleteFromAll, TextField newTag){
        deleteFromAll.setOnAction(
                e -> {
                    if (checkValidTagName(newTag.getText())) {
                        ImageManager.deleteGlobalTag(newTag.getText());
                        newTag.setText("");
                    }
                    SceneManager.swapToPicGrid(directory);
                });
    }
}
