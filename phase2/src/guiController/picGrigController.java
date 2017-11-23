package guiController;

import guiView.PicGrid;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class picGrigController {


    // Only show the option of showing ImageFiles in or under directory if the parent directory has sub-folders
    public static void activateDirectoryFolders(ArrayList<Button> subDirImageButtons, FlowPane pane){

        if (subDirImageButtons.size() != 0) {   // TODO: put this in the controller
            Button show;
            if (!PicGrid.showAll) show = new Button("Press to show all images under this directory (includes subfolder)");
            else show = new Button("Press to show images in this directory (only parent folder)");

            show.setOnAction(
                    e -> {
                        if (PicGrid.showAll) {
                            PicGrid.showAll = false;
                            show.setText("Press to show images in this directory (only parent folder)");
                            pane.getChildren().removeAll(subDirImageButtons);
                        } else {
                            PicGrid.showAll = true;
                            show.setText("Press to show all images under this directory (includes subfolder)");
                            pane.getChildren().addAll(subDirImageButtons);
                        }
                    });
            pane.getChildren().add(show);
        }
    }

}
