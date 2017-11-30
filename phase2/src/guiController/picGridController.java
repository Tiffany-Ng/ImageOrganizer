package guiController;

import ManageImage.ImageFile;
import ManageImage.ImageManager;
import ManageImage.TagManager;
import guiView.PicGridView;
import guiView.SceneManager;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class picGridController {

    public static void independentTags(File dir, Boolean showAll) {

        Stage chooser = new Stage();

        GridPane g = new GridPane();
        g.setVgap(5);
        g.setHgap(5);

        Scene s = new Scene(g);

        ArrayList<String> selectedTags = new ArrayList<>();
        ListView<HBox> listView = new ListView<>();

        independentTags(selectedTags, listView);
        g.add(listView, 1, 1, 1, 1);

        ArrayList<ImageFile> images;
        if(showAll)
            images = ImageManager.getImageFilesByDirectory(dir);
        else
            images = ImageManager.getImageFilesInParentDirectory(dir);
        ArrayList<CheckBox> imageCheckBox = new ArrayList<>();
        ArrayList<ImageFile> imageSelected = new ArrayList<>();

        CheckBox c = new CheckBox();

        c.setOnAction(e -> {

            if (c.isSelected()) {

                imageSelected.addAll(images);

                for (CheckBox check : imageCheckBox) {

                    check.setSelected(true);

                }

            }

            else {

                imageSelected.removeAll(images);
                for (CheckBox check : imageCheckBox) {

                    check.setSelected(false);

                }

            }

        });

        for (ImageFile image : images) {
            CheckBox checkBox = new CheckBox(image.getName());
            imageCheckBox.add(checkBox);

            checkBox.setOnAction(e ->{

                ImageFile currImage = images.get(imageCheckBox.indexOf(checkBox));

                if(checkBox.isSelected())
                    imageSelected.add(currImage);

                else
                    imageSelected.remove(currImage);

            });
        }

        ListView<CheckBox> imageView = new ListView<>();
        imageView.getItems().add(c);
        imageView.getItems().addAll(imageCheckBox);

        g.add(imageView, 3, 1, 1, 1);

        Button add = new Button("Add to");
        add.setTooltip(new Tooltip("Add selected tags to selected images."));
        add.setMaxWidth(Double.MAX_VALUE);

        add.setOnAction(e ->  {

            if (!imageSelected.isEmpty()) {

                for (ImageFile i : imageSelected) {

                    i.addTag(selectedTags);

                }
            }


            deselect(imageCheckBox, listView);


        });


        Button remove = new Button("Remove from");
        remove.setTooltip(new Tooltip("Remove selected tags from selected images."));
        remove.setOnAction(e -> {

            if (!imageSelected.isEmpty()) {

                for (ImageFile i : imageSelected) {

                    i.removeTag(selectedTags);

                }
            }

            deselect(imageCheckBox, listView);

        });

        Button deleteTags = new Button("Delete Tags");
        deleteTags.setTooltip(new Tooltip("Remove selected tags permanently."));
        deleteTags.setMaxWidth(Double.MAX_VALUE);

        deleteTags.setOnAction(e -> {
            if (!selectedTags.isEmpty()) {
                for (String tag : selectedTags)
                    ImageManager.deleteGlobalTag(tag);
            }
            TagManager.getTags().removeAll(selectedTags);
            selectedTags.clear();
            independentTags(selectedTags, listView);

            deselect(imageCheckBox, listView);

        });

        VBox v = new VBox();
        v.getChildren().addAll(add, remove, deleteTags);
        v.setAlignment(Pos.CENTER);
        v.setSpacing(5);

        g.add(v, 2, 1, 1, 1);
        GridPane.setValignment(v, VPos.CENTER);

        chooser.setMaximized(false);
        chooser.setScene(s);
        chooser.initModality(Modality.APPLICATION_MODAL);
        chooser.show();


    }

    private static void deselect(ArrayList<CheckBox> imageCheckBox, ListView<HBox> listView) {

        for (CheckBox check : imageCheckBox)
            check.setSelected(false);

        for (HBox h : listView.getItems())
            ((CheckBox) h.getChildren().get(0)).setSelected(false);

    }

    private static void independentTags(ArrayList<String> selectedTags,  ListView<HBox> listView ) {

        listView.getItems().clear();
        LinkedList<String> tags = TagManager.getTags();
        ArrayList<HBox> tagCheckBox = new ArrayList<>();

        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            HBox v = new HBox();
            v.getChildren().add(checkBox);
            tagCheckBox.add(v);

            checkBox.setOnAction(e ->{

                String currTag = checkBox.getText();

                if(checkBox.isSelected())
                    selectedTags.add(currTag);

                else
                    selectedTags.remove(currTag);

            });
        }

        CheckBox d = new CheckBox();

        d.setOnAction(e -> {

            if (d.isSelected()) {

                selectedTags.addAll(tags);

                for (HBox hBox : tagCheckBox) {

                    ((CheckBox) hBox.getChildren().get(0)).setSelected(true);

                }

            }

            else {

                selectedTags.removeAll(tags);
                for (HBox hBox : tagCheckBox) {

                    ((CheckBox) hBox.getChildren().get(0)).setSelected(false);

                }

            }

        });

        TextField newTag = new TextField();
        newTag.setEditable(true);
        newTag.setOnKeyPressed(k -> {

            if (k.getCode().equals(KeyCode.ENTER)) { // #TODO add way to verify and update dynamically

                TagManager.add(newTag.getText());
                independentTags(selectedTags, listView);

            }

        });

        Button add = new Button("+");
        add.setTooltip(new Tooltip("Create new tag."));
        add.setOnAction(e -> {

            TagManager.add(newTag.getText());
            independentTags(selectedTags, listView);

        });

        HBox firstElement = new HBox();

        firstElement.getChildren().addAll(d, newTag, add);
        firstElement.setSpacing(5);

        listView.getItems().add(firstElement);
        listView.getItems().addAll(tagCheckBox);

    }

    // Only show the option of showing ImageFiles in or under directory if the parent directory has sub-folders
    public static void activateDirectoryFolders(ArrayList<Button> subDirImageButtons, FlowPane pane){

        if (subDirImageButtons.size() != 0) {
            Button show;
            if (!PicGridView.showAll) show = new Button("Press to show all images under this directory (includes subfolder)");
            else show = new Button("Press to show images in this directory (only parent folder)");

            show.setOnAction(
                    e -> {
                        if (PicGridView.showAll) {
                            PicGridView.showAll = false;
                            show.setText("Press to show images in this directory (only parent folder)");
                            pane.getChildren().removeAll(subDirImageButtons);
                        } else {
                            PicGridView.showAll = true;
                            show.setText("Press to show all images under this directory (includes subfolder)");
                            pane.getChildren().addAll(subDirImageButtons);
                        }
                    });
            pane.getChildren().add(show);
        }
    }


    /**
     * Open up an image that a user has selected.
     *
     * @param viewImage Records the user's action to view the image
     * @param img The image file that will be opened
     * @param dir The directory(path) of the image that will be opened
     */
    public static void viewImage(Button viewImage, ImageFile img, File dir){
        viewImage.setOnAction(
                e -> {
                    SceneManager.swapToImageScene(img, dir);
                });
    }


    /**
     * Open up a new set of images from a user selected directory path.
     *
     * @param chooseDirectory Records the user's action to view a new directory of images
     * @param currentStg A reference to the current GUI scene.
     */
    public static void chooseNewDir(Button chooseDirectory, Stage currentStg){
        chooseDirectory.setOnAction(e -> dirController.dirChooser(currentStg));

    }

}