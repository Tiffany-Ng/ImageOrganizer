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
import java.util.ArrayList;

class PicGrid {
  static void picGrid(Stage currentStage, File directory) {
    FlowPane pane = new FlowPane();
    pane.setPadding(new Insets(20, 20, 5, 20));
    pane.setVgap(15);
    pane.setHgap(15);
    pane.setPrefWrapLength(300);

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(pane);
    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);

    Scene scene = new Scene(scrollPane);
    currentStage.setScene(scene);
    currentStage.setMaximized(true);

    Label label = new Label(directory.toString());
    label.setMinWidth(2000);
    pane.getChildren().add(label);

    ImageManager im = new ImageManager();
    im.getImagesFrom(directory.toString());
    ArrayList<Button> toAdd = new ArrayList<>();
    for (ImageFile img : ImageManager.getImageFiles()) {
      Image image = new Image("file:///" + img.getFile().toString(), 200, 200, true, true, true);
      ImageView view = new ImageView(image);
      view.setCache(true);
      view.setCacheHint(CacheHint.SPEED);
      Button button = new Button(img.getName(), view);
      button.setContentDisplay(ContentDisplay.TOP);
      button.setCache(true);
      button.setCacheHint(CacheHint.SPEED);

      button.setOnAction(
          e -> {
            ImageScene toScene = new ImageScene(img);
            currentStage.setScene(toScene.getImageScene());
          });
      toAdd.add(button);
    }
    pane.getChildren().addAll(toAdd);
  }
}
