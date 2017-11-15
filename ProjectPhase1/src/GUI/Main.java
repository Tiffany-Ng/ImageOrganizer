package GUI;

import ManageImage.ImageManager;
import ManageImage.TagManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage currentStage) {
    ImageManager.load();
    TagManager.load();
    DirChooser.dirChooser(currentStage, false);
  }

  @Override
  public void stop() {
    ImageManager.save();
    TagManager.save();
  }
}
