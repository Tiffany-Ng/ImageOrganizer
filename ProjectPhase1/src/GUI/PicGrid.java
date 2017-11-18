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
import java.util.List;

/**
 * Shows a list of thumbnails of all images under the directory.
 */
public class PicGrid {

    private static Stage currentStg;
    public static File dir;
    public static int changeDirPoint = 0;
    public static boolean showAll = true;
    public static ArrayList<Button> imageButtons = new ArrayList<>();

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
        ArrayList<Button> toAdd = new ArrayList<>();
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
                        ImageScene toScene = null;
                        try {
                            toScene = new ImageScene(img, dir, currentStg);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        currentStg.setScene(toScene.getImageScene());
                    });

            if (img.getDirectory().equals(dir)) {
                toAdd.add(viewImage);
                changeDirPoint += 1;
            }else{
                toAdd.add(viewImage);
            }
        }

        return toAdd;
    }

    /**
     * Sets the pane and ImageFiles for GUI in a grid like format.
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

        if (!(changeDirPoint == imageButtons.size())) {
            Button show;
            if (!showAll) show = new Button("Show all images under this directory (includes subfolder)");
            else show = new Button("Show images in this directory");

            show.setOnAction(
                    e -> {
                        if (!showAll) {
                            showAll = true;
                            show.setText("Show images in this directory");
                            pane.getChildren().addAll(imageButtons.subList(changeDirPoint, imageButtons.size()));
                        } else {
                            showAll = false;
                            show.setText("Show all images under this directory (includes subfolder)");
                            pane.getChildren()
                                    .removeAll(imageButtons.subList(changeDirPoint, imageButtons.size()));
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
     * @return  ArrayList<ImageFile> the ImageFiles displayed in PicGrid
     */
    public static ArrayList<ImageFile> getDisplayedFiles() {
        ArrayList<ImageFile> list = ImageManager.getImageFilesByDirectory(PicGrid.dir);

        if (!(PicGrid.changeDirPoint == PicGrid.imageButtons.size()) && !PicGrid.showAll) {
            List<ImageFile> toAdd = list.subList(PicGrid.changeDirPoint, list.size());
            list.clear();
            list.addAll(toAdd);
        }

        return list;
    }
}
