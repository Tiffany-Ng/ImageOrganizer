package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import ManageImage.*;

import java.util.List;

/**
 * GUI of an individual image's information
 */
public class ImageScene {

    /**
     * The image used .
     */
    private ImageFile image;

    private Scene imageScene;

    private GridPane g;

    /**
     * Construct an GUI.ImageScene.
     *
     * @param image
     */
    public ImageScene(ImageFile image) {

        this.image = image;

        g = gridSetup();
        imageScene = new Scene(g);

    }

    /**
     * Setup of the whole screen
     *
     * @return GridPane
     */
    private GridPane gridSetup() {

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(0, 10, 0, 10));

        // image in form of a viewable icon
        ImageView icon = new ImageView();
        icon.setFitWidth(720);
        icon.setFitHeight(480);

        // since we have a class named image, the javafx image needs to be used like this
        // needs the "file://" because image will not understand it is a directory
        icon.setImage(new javafx.scene.image.Image("file:///" + image.getFile().toString()));
        layout.add(icon, 1, 1, 2, 2);

        // flowPane for image information
        FlowPane f = flowSetup();
        layout.add(f, 3, 1, 1, 3);

        // go to main screen
        Button back = new Button();
        back.setText("<- Back");
        layout.add(back, 0, 0, 1, 1);

        // #TODO setup the action to go back to main screen
        back.setOnAction(e -> {



        });

        return layout;

    }

    /**
     * Setup the image's information in a flowPane.
     *
     * @return FlowPane
     */
    private FlowPane flowSetup() {

        // initial values
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);

        // sub-flowPane to hold the tags
        FlowPane f = new FlowPane(Orientation.HORIZONTAL, 5, 5);
        f.setPadding(new Insets(5));

        // make all the tags
        List<String> tags = image.getTags();
        for (String t : tags) {

            // makes all tags as clickable buttons
            Button tag = new Button(t);

            // #TODO make sure tags disappear when clicked, and is visually shown
            tag.setOnAction(e -> image.removeTag(tag.toString()));
            f.getChildren().add(tag);

        }

        flow.getChildren().add(new ScrollPane(f));

        // image directory
        Text directory = new Text();
        directory.setText(image.getDirectory().toString());
        flow.getChildren().add(directory);

        // image log
        Log imageLog = image.getLog();
        StringBuilder logs = new StringBuilder();

        // add all logs as a line
        for (Entry e: imageLog) {

            logs.append(e.toString()).append(" \n");

        }

        // represent log as textArea
        TextArea log = new TextArea(logs.toString());
        log.setWrapText(true);
        log.setEditable(false);
        flow.getChildren().add(log);

        return flow;

    }

    public Scene getImageScene() { return imageScene; }

}
