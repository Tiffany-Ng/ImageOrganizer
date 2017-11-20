package GUI;

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
     * The index in imageButtons where the directory of all ImageFiles after that point is not in the chosen parent directory but the sub-folders
     */
    private static int changeDirPoint = 0;

    /**
     * True if user wants to see all images under the chosen directory (includes sub-folders)
     */
    private static boolean showAll = true;

    /**
     * Stores the buttons with image which leads to ImageScene on click
     */
    private static ArrayList<Button> imageButtons = new ArrayList<>();

    PicGrid(Stage currentStg, File dir) {
        PicGrid.currentStg = currentStg;
        PicGrid.dir = dir;
        imageButtons = gatherImages();
    }

    /**
     * Return a view of all images form a directory, onto the GUI
     *
     * @return ArrayList an array list of all clickable image buttons
     */
    private static ArrayList<Button> gatherImages() {
        ArrayList<Button> sameDirectory = new ArrayList<>();
        ArrayList<Button> differentDirectory = new ArrayList<>();
        changeDirPoint = 0;
        for (ImageFile img : ImageManager.getImageFilesByDirectory(dir)) {

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

            viewImage.setOnAction(
                    e -> {
                        try {
                            ImageScene toScene = new ImageScene(img, dir, currentStg);
                            currentStg.setScene(toScene.getImageScene());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    });

            if (img.getDirectory().equals(dir)) {
                sameDirectory.add(viewImage);
                changeDirPoint += 1;
            } else {
                differentDirectory.add(viewImage);
            }
        }

        sameDirectory.addAll(differentDirectory);
        return sameDirectory;
    }

    /**
     * Sets the pane and ImageFiles for GUI in a grid like format.
     * Functionality of panes from: https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm Date:  Nov 9, 2017
     */
    void picGrid() {

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
        chooseDirectory.setOnAction(e -> DirChooser.dirChooser(currentStg));

        pane.getChildren().add(chooseDirectory);

        // Only show the option of showing ImageFiles in or under directory if the parent directory has sub-folders
        if (!(changeDirPoint == imageButtons.size())) {
            Button show;
            if (!showAll) show = new Button("Press to show all images under this directory (includes subfolder)");
            else show = new Button("Press to show images in this directory (only parent folder)");

            show.setOnAction(
                    e -> {
                        if (!showAll) {
                            showAll = true;
                            show.setText("Press to show images in this directory (only parent folder)");
                            pane.getChildren().addAll(imageButtons.subList(changeDirPoint, imageButtons.size()));
                        } else {
                            showAll = false;
                            show.setText("Press to show all images under this directory (includes subfolder)");
                            pane.getChildren().removeAll(imageButtons.subList(changeDirPoint, imageButtons.size()));
                        }
                    });
            pane.getChildren().add(show);
        }

        Label currentDirectory = new Label("Parent directory: " + dir.toString());
        currentDirectory.setMinWidth(2000);
        pane.getChildren().add(currentDirectory);

        pane.getChildren().addAll(imageButtons);
        if (!showAll)
            pane.getChildren().removeAll(imageButtons.subList(changeDirPoint, imageButtons.size()));
    }

    /**
     * Returns the ImageFiles displayed in PicGrid
     *
     * @return ArrayList<ImageFile> the ImageFiles displayed in PicGrid
     */
    public static ArrayList<ImageFile> getDisplayedFiles() {
        ArrayList<ImageFile> list = ImageManager.getImageFilesByDirectory(PicGrid.dir);

        if (!showAll)
            for (ImageFile file : ImageManager.getImageFilesByDirectory(PicGrid.dir))
                if (!file.getDirectory().equals(dir))
                    list.remove(file);

        return list;
    }
}
