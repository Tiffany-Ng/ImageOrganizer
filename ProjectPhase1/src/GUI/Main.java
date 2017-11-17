package GUI;

import ManageImage.ImageManager;
import ManageImage.TagManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Where the program run from.
 */
public class Main extends Application {

  //https://www.loggly.com/ultimate-guide/java-logging-basics/ (Nov 16, 2017)
  public static Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    ConsoleHandler handler = new ConsoleHandler();

    handler.setLevel(Level.ALL);
    logger.addHandler(handler);
    logger.setLevel(Level.ALL);

    launch(args);
  }

  @Override
  public void start(Stage currentStage) {
    ImageManager.load();
    TagManager.load();
    DirChooser.dirChooser(currentStage);
  }

  @Override
  public void stop() {
    ImageManager.save();
    TagManager.save();
  }
}
