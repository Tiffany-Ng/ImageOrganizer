package GUI;

import ManageImage.ImageManager;
import ManageImage.TagManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

class DirChooser {

    /** Represents the directory that was opened by a user */
    static File directory;

    static File dirChooser(Stage currentStage, boolean getString){

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

        dirChooserBtn.setOnAction(
                e -> {
                    DirectoryChooser dirChooser = new DirectoryChooser();
                    File directory = dirChooser.showDialog(currentStage);
                    if (directory != null) {
                        dirTextField.setText(directory.toString());
                        error.setVisible(false);
                    }
                });

        goBtn.setOnAction(
                e -> {
                    directory = new File(dirTextField.getText());

                    if (directory.isDirectory()) {
                        if (!getString) {
                            //Loads in tags from tags.ser
                            TagManager tm = new TagManager();
                            ImageManager im = new ImageManager();
                            im.createImagesFromDirectory(directory.toString());
                            chooser.hide();
                            PicGrid.picGrid(currentStage, directory, im);
                            currentStage.show();
                        }

                    } else {
                        error.setVisible(true);
                    }
                });

        chooser.setMaximized(false);
        chooser.setScene(scene);
        chooser.initModality(Modality.APPLICATION_MODAL);
        chooser.show();

        return directory;
    }

}
