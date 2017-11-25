package guiController;

import ManageImage.ImageFile;
import ManageImage.ImageManager;
import ManageImage.TagManager;
import guiView.PicGridView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class picGrigController {

    public static void independentTags(File dir) {

        Stage chooser = new Stage();

        GridPane g = new GridPane();
        g.setVgap(5);
        g.setHgap(5);

        Scene s = new Scene(g);

        LinkedList<String> tags = TagManager.getTags();
        ArrayList<CheckBox> tagCheckBox = new ArrayList<>();
        ArrayList<String> selectedTags = new ArrayList<>();

        for (String tag : tags) {
            CheckBox checkBox = new CheckBox(tag);
            tagCheckBox.add(checkBox);

            checkBox.setOnAction(e ->{

                String currTag = checkBox.getText();

                if(checkBox.isSelected())
                    selectedTags.add(currTag);

                else
                    selectedTags.remove(currTag);

            });
        }

        ListView<CheckBox> listView = new ListView<>();
        listView.getItems().addAll(tagCheckBox);

        g.add(listView, 1, 1, 1, 1);

        ArrayList<ImageFile> images = ImageManager.getImageFilesInSubDirectory(dir);
        ArrayList<CheckBox> imageCheckBox = new ArrayList<>();
        ArrayList<ImageFile> imageSelected = new ArrayList<>();

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
        imageView.getItems().addAll(imageCheckBox);

        g.add(imageView, 3, 1, 1, 1);

        Button add = new Button("Add to");
        Button remove = new Button("Remove from");

        VBox v = new VBox();
        v.getChildren().addAll(add, remove);

        g.add(v, 2, 1, 1, 1);

        chooser.setMaximized(false);
        chooser.setScene(s);
        //Cite: https://www.programcreek.com/java-api-examples/index.php?class=javafx.stage.Stage&method=showAndWait Date: Nov 5, 2017
        chooser.initModality(Modality.APPLICATION_MODAL);
        chooser.show();


    }

    // Only show the option of showing ImageFiles in or under directory if the parent directory has sub-folders
    public static void activateDirectoryFolders(ArrayList<Button> subDirImageButtons, FlowPane pane){

        if (subDirImageButtons.size() != 0) {   // TODO: put this in the controller
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

}
