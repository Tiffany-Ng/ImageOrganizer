package ManageImage;

import javafx.scene.image.ImageView;

public class NormalFilter implements FilterStrategy{
    @Override
    public ImageView applyFilter(ImageView imageView) {
        imageView.setEffect(null);
        return imageView;
    }
}
