package GUI;

import ManageImage.ImageManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage currentStage) {
    DirChooser.dirChooser(currentStage);
    currentStage.show();
  }

  @Override
  public void stop() {
    ImageManager.save();
  }
}
