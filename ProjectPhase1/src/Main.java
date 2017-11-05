import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.DirectoryChooser;
import java.io.File;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage chooseDir) {
    chooseDir.setTitle("Turtle Image Tag View Manager!");

    GridPane pane1 = new GridPane();
    GridPane pane2 = new GridPane();
    Scene scene1 = new Scene(pane1, 500, 250);
    Scene scene2 = new Scene(pane2);

    Button dirChooserBtn = new Button();
    dirChooserBtn.setText("...");

    Button goBtn = new Button();
    goBtn.setText("Go");

    TextField dirTextField = new TextField();
    dirTextField.setPrefWidth(350);
    dirTextField.setText("Input Directory");

    Text instruction = new Text();
    instruction.setText("Please select/type in a directory:");

    Text error = new Text();
    error.setText("Please select a working directory.");
    error.setFill(Color.RED);
    GridPane.setHalignment(error, HPos.RIGHT);
    pane1.add(error, 4, 8);
    error.setVisible(false);

    pane1.setHgap(10);
    pane1.setVgap(10);
    pane1.setPadding(new Insets(5, 5, 5, 5));

    GridPane.setHalignment(dirTextField, HPos.LEFT);
    pane1.add(dirTextField, 4, 7);
    pane1.add(dirChooserBtn, 5, 7);
    pane1.add(instruction, 4, 6);
    pane1.add(goBtn, 5, 8);

    dirChooserBtn.setOnAction(
        e -> {
          DirectoryChooser dirChooser = new DirectoryChooser();
          File directory = dirChooser.showDialog(chooseDir);
          if (directory != null) {
            dirTextField.setText(directory.toString());
            error.setVisible(false);
          }
        });

    goBtn.setOnAction(
        e -> {
          File directory = new File(dirTextField.getText());
          if (directory.isDirectory()) {
            chooseDir.setScene(scene2);
            chooseDir.setMaximized(true);
            System.out.print(directory);
          } else {
            error.setVisible(true);
          }
        });

    chooseDir.setScene(scene1);
    chooseDir.show();
  }
}
