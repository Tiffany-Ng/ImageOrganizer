package guiController;

import ManageImage.*;
import guiView.ImageSceneView;
import guiView.Main;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;

public class imageSceneController {

    private static FlowPane flowLayout;

    private static ImageFile image;

    private static ArrayList<String> tagsToAdd;

    private static ArrayList<String> tagsToDelete;

    /**
     * Setter for the tags to be deleted from an image
     *
     * @param tagsToDelete collection of tags which need to be deleted from an image.
     */
    public static void setTagsToDelete(ArrayList<String> tagsToDelete) {
        imageSceneController.tagsToDelete = tagsToDelete;
    }

    /**
     * Setter for the tags to be added to an image
     *
     * @param tagsToAdd collection of tags which need to be added to an image.
     */
    public static void setTagsToAdd(ArrayList<String> tagsToAdd) {
        imageSceneController.tagsToAdd = tagsToAdd;
    }


    /**
     * Setter for the image environment opened by a user.
     *
     * @param flowLayout The user's current image environment
     */
    public static void setFlowLayout(FlowPane flowLayout) {
        imageSceneController.flowLayout = flowLayout;
    }

    /**
     * Setter for the image file opened by a user.
     *
     * @param image The image that has been opened by a user.
     */
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
    private static void createAlert(String title, String header, String content) {

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
    public static void updateLog(TextArea log) {

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
    static public void imageNameUpdate(ComboBox<String> imageNames, TextField name) {

        name.setText(image.getName());
        if (!(imageNames.getItems() == null)) {
            imageNames.getItems().clear();
        }

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
    public static FlowPane addClickableTags() {

        flowLayout.getChildren().clear();
        //https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        LinkedList<String> tags = TagManager.getTags();
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            checkBoxes.add(checkBox);
            if (image.getTags().contains(tag))
                checkBox.setSelected(true);

            checkBox.setOnAction(e -> {
                String text = checkBox.getText();
                if (checkBox.isSelected()) {
                    tagsToAdd.add(text);
                    tagsToDelete.remove(text);
                } else {
                    tagsToDelete.add(text);
                    tagsToAdd.remove(text);
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
    public static void revertOldTagName(Button revertName, ComboBox<String> imageNames, TextArea log, TextField name) {
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
                        addClickableTags();
                        imageNameUpdate(imageNames, name);
                    }
                });
    }


    /**
     * cite : https://stackoverflow.com/questions/13880638/how-do-i-pick-up-the-enter-key-being-pressed-in-javafx2
     * rename the image to the name collected from the relevant texBox(on hitting enter)
     */
    private static void renameImageFile(TextField name, TextArea log, ComboBox imageNames) {

        boolean success = image.rename(name.getText());
        imageSceneController.addClickableTags();
        imageSceneController.updateLog(log);
        imageSceneController.imageNameUpdate(imageNames, name);

        if (!success) {

            imageSceneController.createAlert("Invalid Name", "The name you entered is invalid.",
                    "A name should not contain ' @' and the name " + name.getText() + " must be available");
        }
    }


    /**
     * Change the image to the new-name decided by the user.
     *
     * @param imageNewName the container of the new name(user event is recorded with an enter)
     * @param log          Recorder of all changes made by a user
     * @param imageNames   A collection of the previous names that the image has had.
     */
    public static void changeToNewName(TextField imageNewName, TextArea log, ComboBox imageNames) {
        imageNewName.setOnKeyPressed(
                k -> {
                    if (k.getCode().equals(KeyCode.ENTER)) {
                        imageSceneController.renameImageFile(imageNewName, log, imageNames);
                    }
                });
    }

    /**
     * Change the image to the new-name decided by the user.
     *
     * @param rename       The button which records that a user has decided to change image name
     * @param imageNewName the container of the new name(user event is recorded a button click)
     * @param log          Recorder of all changes made by a user
     * @param imageNames   A collection of the previous names that the image has had.
     */
    public static void renameButtonClick(Button rename, TextField imageNewName, ComboBox imageNames, TextArea log) {
        rename.setOnAction(
                e -> {
                    imageSceneController.renameImageFile(imageNewName, log, imageNames);
                });
    }

    /**
     * Open the a new directory of images.
     *
     * @param openNewDir The button which records that a user wants to view the a new directory of images.
     */
    public static void openImageDirectory(Button openNewDir, boolean parent) {
        openNewDir.setOnAction(
                e -> {
                    try {
                        File directory = image.getDirectory();
                        if (parent)
                            directory = image.getDirectory().getParentFile();

                        if (directory == null) {
                            createAlert("Parent folder does not exist", "Error - open parent folder", "Cannot open a folder that does not exits, opening current directory");
                            directory = image.getDirectory();
                        }
                        // https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/ Date: Nov 29 2017
                        String OS = System.getProperty("os.name").toLowerCase();

                        // This is for the Teaching Lab computers
                        if (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0) {
                            // https://bb-2017-09.teach.cs.toronto.edu/t/cdf-computers-not-allowing-javafx-to-open-files/1786/4 Date: Nov 29 2017
                            Runtime.getRuntime().exec("xdg-open " + directory);
                        } else {
                            // https://stackoverflow.com/questions/15875295/open-a-folder-in-explorer-using-java Date: Nov 29 2017
                            Desktop.getDesktop().open(directory);
                        }

                    } catch (IOException ex) {
                        imageSceneController.createAlert("Open directory error", "Execution failed",
                                "The execution is not supported on this computer");
                        Main.logger.log(Level.SEVERE, "Can't open directory", ex);
                    }
                });
    }

    /**
     * Add a new tag to an image
     *
     * @param addTag       Button which adds the new tag to the image
     * @param newTag       Name of the new tah
     * @param log          Recorder of all changes made by a user
     * @param imageNames   A collection of the previous names that the image has had.
     * @param imageNewName The new name of the image, including the newly added tag
     */
    public static void addTag(Button addTag, TextField newTag, TextArea log, ComboBox<String> imageNames, TextField imageNewName) {
        addTag.setOnAction(
                e -> {
                    if (checkValidTagName(newTag.getText())) {
                        boolean success = image.addTag(newTag.getText());
                        if (success) {
                            newTag.setText("");
                        } else {
                            imageSceneController.createAlert("Add Tag Error", "The tag '" + newTag.getText() + "' was not added successfully",
                                    "Tag imageNewName contains ' @', the tag already exists, or the file imageNewName with the additional tag is occupied");
                        }
                        imageSceneController.addClickableTags();
                        imageSceneController.updateLog(log);
                        imageSceneController.imageNameUpdate(imageNames, imageNewName);
                    }
                });
    }

    /**
     * Records the tag changes made by a user based on the check-box selections.
     *
     * @param updateTags   Button which records all the tag changes for an image
     * @param log          Recorder of all changes made by a user
     * @param imageNames   A collection of the previous names that the image has had
     * @param imageNewName The new name of the image, reflecting all changes made to the tags
     */
    public static void updateTags(Button updateTags, TextArea log, ComboBox imageNames, TextField imageNewName) {
        updateTags.setOnAction(e -> {
            if (tagsToAdd.size() != 0)
                image.addTag(tagsToAdd);
            if (tagsToDelete.size() != 0)
                image.removeTag(tagsToDelete);

            imageSceneController.addClickableTags();
            imageSceneController.updateLog(log);
            imageSceneController.imageNameUpdate(imageNames, imageNewName);

            tagsToAdd.clear();
            tagsToDelete.clear();
        });
    }

    /**
     * Moves the ImageFile up or down one directory
     */
    public static void moveFile(Button moveBtn, boolean up) throws IOException {
        moveBtn.setOnAction(e -> {
            try {
                if (up)
                    image.move(image.getDirectory().getParentFile());
                else
                    image.move(image.getDirectory());

            } catch (IOException e1) {
                Main.logger.warning("Cannot move file");
            }
        });
    }


}
