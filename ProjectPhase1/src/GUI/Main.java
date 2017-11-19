package GUI;

import ManageImage.ImageManager;
import ManageImage.TagManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Where the program run from.
 */
public class Main extends Application {

    //Citation for all logger related code: https://www.loggly.com/ultimate-guide/java-logging-basics/ Date: Nov 16, 2017
    public static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        LogManager.getLogManager().reset();

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.SEVERE);
        logger.addHandler(handler);
        logger.setLevel(Level.OFF);

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
