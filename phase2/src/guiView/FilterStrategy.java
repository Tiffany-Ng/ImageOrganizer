package guiView;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

// Adapted from: https://dzone.com/articles/design-patterns-strategy Date: Nov 21, 207
interface FilterStrategy {
    ImageView applyFilter(ImageView image);
}

class BlackAndWhiteFilter implements FilterStrategy{
    @Override
    public ImageView applyFilter(ImageView imageView) {
        // Adapted from: https://stackoverflow.com/questions/43068319/how-to-create-javafx-16-bit-greyscale-images Date: Nov 21, 207

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        imageView.setEffect(colorAdjust);

        return imageView;
    }
}
