package ManageImage;

import javafx.scene.image.ImageView;

public class NormalFilter implements FilterStrategy{
    @Override
    public void applyFilter(ImageView imageView) {
        imageView.setEffect(null);
    }
}
