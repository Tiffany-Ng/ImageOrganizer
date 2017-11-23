package guiView;

import guiController.Controller;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ManageImage.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Shows a list of thumbnails of all images under the directory.
 *
 * @author Allan Chang 1003235983
 * @author Prynciss Ng 1003136091
 * @author Amarnath Parthiban 1003193518
 * @author Akshat Nigam 1002922732
 */
public class PicGrid {

    /**
     * The main stage to show the user
     */
    private static Stage currentStg;

    /**
     * Parent directory that user chose
     */
    private static File dir;

    /**
     * True if user wants to see all images under the chosen directory (includes sub-folders)
     */
    private static boolean showAll = true;

    /**
     * Stores the buttons with image which leads to ImageScene on click
     */
    private ArrayList<Button> imageButtons = new ArrayList<>();

    /**
     * Stores the buttons with image in sub-folders which leads to ImageScene on click
     */
    private ArrayList<Button> subDirImageButtons = new ArrayList<>();

    public PicGrid(Stage currentStg, File dir) {
        PicGrid.currentStg = currentStg;
        PicGrid.dir = dir;
        imageButtons = gatherImages(ImageManager.getImageFilesInParentDirectory(dir));
        subDirImageButtons = gatherImages(ImageManager.getImageFilesInSubDirectory(dir));
        imageButtons.addAll(subDirImageButtons);
    }

    /**
     * Return a view of all images form a directory, onto the guiController
     *
     * @return ArrayList an array list of all clickable image buttons
     */
    private static ArrayList<Button> gatherImages(ArrayList<ImageFile> files) {
        ArrayList<Button> buttons = new ArrayList<>();

        for (ImageFile img : files) {

            Image image = new Image("file:///" + img.getFile().toString(), 200, 200, true, true, true);
            ImageView view = new ImageView(image);

            // Source:
            // https://stackoverflow.com/questions/18911186/how-do-setcache-and-cachehint-work-together-in-javafx (Date: Nov 9, 2017)
            view.setCache(true);
            view.setCacheHint(CacheHint.SPEED);

            Button viewImage = new Button(img.getName(), view);
            viewImage.setContentDisplay(ContentDisplay.TOP);

            // Source:
            // https://stackoverflow.com/questions/18911186/how-do-setcache-and-cachehint-work-together-in-javafx (Date: Nov 9, 2017)
            viewImage.setCache(true);
            viewImage.setCacheHint(CacheHint.SPEED);

            viewImage.setOnAction(   // TODO: possibly in controller...(np)
                    e -> {
                        try {
                            ImageScene toScene = new ImageScene(img, dir, currentStg);
                            currentStg.setScene(toScene.getImageScene());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });

            buttons.add(viewImage);

        }
        return buttons;
    }

    /**
     * Sets the pane and ImageFiles for guiController in a grid like format.
     * Functionality of panes from: https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm Date:  Nov 9, 2017
     */

    public void picGrid() {

        currentStg.setTitle("Image Viewer - List images");

        FlowPane pane = new FlowPane();
        pane.setPadding(new Insets(20, 20, 5, 20));
        pane.setVgap(15);
        pane.setHgap(15);
        pane.setPrefWrapLength(300);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        currentStg.setResizable(true);
        currentStg.setMaximized(true);
        scrollPane.setMinViewportWidth(currentStg.getWidth());
        scrollPane.setMinViewportHeight(currentStg.getHeight());

        Scene scene = new Scene(scrollPane);
        currentStg.setScene(scene);

        Button chooseDirectory = new Button("Select directory");
        chooseDirectory.setOnAction(e -> Controller.dirChooser(currentStg));

        pane.getChildren().add(chooseDirectory);

        // Only show the option of showing ImageFiles in or under directory if the parent directory has sub-folders
        if (subDirImageButtons.size() != 0) {   // TODO: put this in the controller
            Button show;
            if (!showAll) show = new Button("Press to show all images under this directory (includes subfolder)");
            else show = new Button("Press to show images in this directory (only parent folder)");

            show.setOnAction(
                    e -> {
                        if (showAll) {
                            showAll = false;
                            show.setText("Press to show images in this directory (only parent folder)");
                            pane.getChildren().removeAll(subDirImageButtons);
                        } else {
                            showAll = true;
                            show.setText("Press to show all images under this directory (includes subfolder)");
                            pane.getChildren().addAll(subDirImageButtons);
                        }
                    });
            pane.getChildren().add(show);
        }

        Label currentDirectory = new Label("Parent directory: " + dir.toString());
        currentDirectory.setMinWidth(2000);
        pane.getChildren().add(currentDirectory);

        pane.getChildren().addAll(imageButtons);

        if (!showAll && subDirImageButtons.size() != 0)
            pane.getChildren().removeAll(subDirImageButtons);
    }

    /**
     * Returns true if user chose to view all images (in and under directory), false if viewing only in parent folder
     *
     * @return boolean true if user chose to view all images (in and under directory), false if viewing only in parent folder
     */
    public static boolean getShowAll() {
        return showAll;
    }

    /**
     * Returns the chosen directory by the user
     *
     * @return boolean true if user chose to view all images (in and under directory), false if viewing only in parent folder
     */
    public static File getDirectory() {
        return dir;
    }
}
